package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.consumer.Consumer;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.producer.ProducerRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class Foreman extends Thread {

    private final ApplicationContext appContext;
    private final Engine engine;
    private final ExecutorService producerService;
    private final ExecutorService ioService;
    private final ExecutorService cpuService;
    private final Logger log;

    List<Producer> producerList = new ArrayList<>();

    public Foreman
    (
        ApplicationContext applicationContext,
        Engine engine,
        @Qualifier("producerExecutor") ExecutorService producerService,
        @Qualifier("ioIntensiveExecutor") ExecutorService ioService,
        @Qualifier("cpuIntensiveExecutor") ExecutorService cpuService,
        Logger log)
    {
        this.appContext = applicationContext;
        this.engine = engine;
        this.producerService = producerService;
        this.ioService = ioService;
        this.cpuService = cpuService;
        this.log = log;
    }

    // the foreman might take into consideration the average time required for a consumer to consume a task, the queue capacity
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
                    break;
                }
                runProducers();
                Thread.sleep(1000);
            }
            log.info("Foreman stopped, no tasks left to execute!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addProducer(Producer producer) {
        this.producerList.add(producer);
    }

    private void runProducers() {
        for (int i=0; i<producerList.size(); i++) {
            producerService.submit(new ProducerRunner(this.engine, producerList.get(i), this.log));
        }
        producerList.clear();
    }

    private boolean needToCreateConsumer() {
        return engine.blockingDeque.size() > 0;
    }

    private void createConsumers() {
        int currentCpuIntensiveTasks = this.engine.cpuIntensiveTasks.get();
        if (currentCpuIntensiveTasks > 0) {
            log.info("Creating new consumer for cpu operations and adding to cpu service!");
            log.info("Current cpu intensive tasks: " + currentCpuIntensiveTasks);
            Consumer consumer = appContext.getBean("cpuConsumer", Consumer.class);
            cpuService.submit(consumer);
        }
        int currentIoIntensiveTasks = this.engine.ioIntensiveTasks.get();
        if (currentIoIntensiveTasks > 0) {
            log.info("Creating new consumer for io operations and adding to io service");
            log.info("Current io intensive tasks: " + currentIoIntensiveTasks);
            Consumer consumer = appContext.getBean("ioConsumer", Consumer.class);
            ioService.submit(consumer);
        }
    }
}
