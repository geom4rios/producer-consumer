package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.consumer.Consumer;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.producer.ProducerRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class Foreman extends Thread {

    private final ApplicationContext appContext;
    private final Engine engine;
    private final ExecutorService producerService;
    private final ExecutorService ioService;
    private final ExecutorService cpuService;
    private final ExecutorService memoryService;
    private final Logger log;

    List<Producer> producerList = new ArrayList<>();

    public Foreman
            (
                    ApplicationContext applicationContext,
                    Engine engine,
                    @Qualifier("producerExecutor") ExecutorService producerService,
                    @Qualifier("ioIntensiveExecutor") ExecutorService ioService,
                    @Qualifier("cpuIntensiveExecutor") ExecutorService cpuService,
                    @Qualifier("memoryIntensiveExecutor") ExecutorService memoryService,
                    Logger log
            )
    {
        this.appContext = applicationContext;
        this.engine = engine;
        this.producerService = producerService;
        this.ioService = ioService;
        this.cpuService = cpuService;
        this.memoryService = memoryService;
        this.log = log;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Starting foreman");
        this.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                boolean needToCreateConsumer = needToCreateConsumer();
                if (needToCreateConsumer) {
                    createConsumers();
                }
                if (!needToCreateConsumer && producerList.isEmpty()) {
                    log.info("Foreman waiting for producers");
                    synchronized (this){
                        this.wait();
                    }
                }
                runProducers();
                Thread.sleep(1000);
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
            if (this.getState().equals(State.WAITING)) {
                this.notify();
            }
        }
    }

    private void runProducers() {
        for (Producer producer : producerList) {
            ProducerRunner producerRunner = new ProducerRunner(this.engine, producer, this.log);
            producerService.submit(producerRunner);
            this.engine.numberOfProducersRunning.incrementAndGet();
        }
        producerList.clear();
    }

    private boolean needToCreateConsumer() {
        return engine.concurrentLinkedDeque.size() > 0;
    }

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
        this.engine.numberOfConsumersRunning.incrementAndGet();
        this.engine.ioIntensiveConsumersRunning.incrementAndGet();
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
        this.engine.numberOfConsumersRunning.incrementAndGet();
        this.engine.cpuIntensiveConsumersRunning.incrementAndGet();
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
        this.engine.numberOfConsumersRunning.incrementAndGet();
        this.engine.memoryIntensiveConsumersRunning.incrementAndGet();
    }
}
