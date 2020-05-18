package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.consumer.Consumer;
import com.geom4rios.javaproducerconsumer.consumer.TaskDistributor;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.producer.ProducerRunner;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class is always up and running and is basically the orchestrator of the application. <br>
 * Responsible to instantiate/spawn the ProducerRunner threads that will write to the common queue. <br>
 * Responsible to instantiate the {@link TaskDistributor TaskDistributor} that will read from the common queue and write to the {@link TaskType task types} specific queues <br>
 * Responsible to instantiate the {@link TaskType TaskType} specific {@link Consumer Consumer} threads that will read and consume tasks from their {@link TaskType TaskType} specific queue. <br>
 * In case there an no tasks pending for production or consumption then the foreman goes into wait mode.
 */
@Component
public class Foreman extends Thread {

    private final ApplicationContext appContext;
    private final Engine engine;
    private final ExecutorService distributorService;
    private final ExecutorService producerService;
    private final ExecutorService ioService;
    private final ExecutorService cpuService;
    private final ExecutorService memoryService;
    private final TaskDistributor taskDistributor;
    private final Logger log;

    List<Producer> producerList = new CopyOnWriteArrayList<>(); // this allows us to add more producers while running the current producers

    public Foreman
            (
                    ApplicationContext applicationContext,
                    Engine engine,
                    @Qualifier("distributorExecutor") ExecutorService distributorService,
                    @Qualifier("producerExecutor") ExecutorService producerService,
                    @Qualifier("ioIntensiveExecutor") ExecutorService ioService,
                    @Qualifier("cpuIntensiveExecutor") ExecutorService cpuService,
                    @Qualifier("memoryIntensiveExecutor") ExecutorService memoryService,
                    TaskDistributor taskDistributor,
                    Logger log
            )
    {
        this.appContext = applicationContext;
        this.engine = engine;
        this.distributorService = distributorService;
        this.producerService = producerService;
        this.ioService = ioService;
        this.cpuService = cpuService;
        this.memoryService = memoryService;
        this.taskDistributor = taskDistributor;
        this.log = log;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // once we receive the application started event then the foreman is instantiated
        log.info("Starting foreman");
        this.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                // check if we need to create consumers
                boolean needToCreateConsumer = needToCreateConsumer();
                if (needToCreateConsumer) {
                    createConsumers();
                }
                // if no consumers are needed and no producer is registered then go to wait mode
                if (!needToCreateConsumer && producerList.isEmpty()) {
                    log.info("Foreman waiting for producers");
                    synchronized (this) {
                        this.wait();
                    }
                }
                runProducers();
                runDistributor();
                Thread.sleep(1000); // sleep for 1 second before re-iterating in order to free resources and let other threads do their work
            }
        } catch (InterruptedException e) {
            log.info("Foreman stopped unexpectedly!");
            e.printStackTrace();
        } finally {
            this.memoryService.shutdown();
            this.ioService.shutdown();
            this.cpuService.shutdown();
            this.producerService.shutdown();
        }
    }

    public void addProducer(Producer producer) {
        this.producerList.add(producer);
        synchronized (this) {
            // get a lock of the object and in case we are in waiting mode then notify that we just got a producer added into our list
            if (this.getState().equals(State.WAITING)) {
                this.notify();
            }
        }
    }

    /**
     * This method will instantiate one {@link ProducerRunner} instance for every {@link Producer} registered/added to the foreman's list. <br>
     * Once all {@link ProducerRunner} instances are submitted to their thread pool then the foreman clears the list.
     */
    private void runProducers() {
        for (Producer producer : producerList) {
            ProducerRunner producerRunner = new ProducerRunner(this.engine, producer, this.log);
            producerService.submit(producerRunner);
        }
        producerList.clear();
    }

    private void runDistributor() {
        distributorService.submit(taskDistributor);
    }

    /**
     * If all queues are empty then return false otherwise we need to create consumers so return true.
     *
     * @return true in case at least one {@link com.geom4rios.javaproducerconsumer.task.Task} exists in the shared queue or the {@TaskType TaskType} specific queues.
     */
    private boolean needToCreateConsumer() {
        return engine.concurrentLinkedDeque.size() > 0 || engine.memoryConcurrentLinkedDeque.size() > 0 || engine.ioConcurrentLinkedDeque.size() > 0 || engine.cpuConcurrentLinkedDeque.size() > 0;
    }

    /**
     * Check which {@link TaskType TaskType} specific consumers to create.
     */
    private void createConsumers() {
        if (shouldCreateCpuConsumer()) {
            createCpuConsumer();
        }
        if (shouldCreateIoConsumer()) {
            createIOConsumer();
        }
        if (shouldCreateMemoryConsumer()) {
            createMemoryConsumer();
        }
    }

    private boolean shouldCreateIoConsumer() {
        int currentIoIntensiveTasks = this.engine.ioIntensiveTasks.get();
        if (currentIoIntensiveTasks > 0) {
            int maxPoolSize = ((ThreadPoolExecutor) ioService).getMaximumPoolSize();
            return engine.ioIntensiveConsumersRunning.get() < (maxPoolSize * 2);
        }
        return false;
    }

    private void createIOConsumer() {
        log.info("Creating new consumer for io operations and adding to io service");
        Consumer consumer = appContext.getBean("ioConsumer", Consumer.class);
        ioService.submit(consumer);
        this.engine.increaseConsumersRunningByType(TaskType.IO_INTENSIVE);
    }

    private boolean shouldCreateCpuConsumer() {
        int currentCpuIntensiveTasks = this.engine.cpuIntensiveTasks.get();
        if (currentCpuIntensiveTasks > 0) {
            int maxPoolSize = ((ThreadPoolExecutor) cpuService).getMaximumPoolSize();
            return engine.cpuIntensiveConsumersRunning.get() < (maxPoolSize * 2);
        }
        return false;
    }

    private void createCpuConsumer() {
        log.info("Creating new consumer for cpu operations and adding to cpu service!");
        Consumer consumer = appContext.getBean("cpuConsumer", Consumer.class);
        cpuService.submit(consumer);
        this.engine.increaseConsumersRunningByType(TaskType.CPU_INTENSIVE);
    }

    private boolean shouldCreateMemoryConsumer() {
        int currentMemoryIntensiveTasks = this.engine.memoryIntensiveTasks.get();
        if (currentMemoryIntensiveTasks > 0) {
            int maxPoolSize = ((ThreadPoolExecutor) memoryService).getMaximumPoolSize();
            return engine.memoryIntensiveConsumersRunning.get() < (maxPoolSize * 2);
        }
        return false;
    }

    private void createMemoryConsumer() {
        log.info("Creating new consumer for memory operations and adding to memory service");
        Consumer consumer = appContext.getBean("memoryConsumer", Consumer.class);
        memoryService.submit(consumer);
        this.engine.increaseConsumersRunningByType(TaskType.MEMORY_INTENSIVE);
    }
}
