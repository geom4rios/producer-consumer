package com.geom4rios.javaproducerconsumer;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class Foreman extends Thread {

    private final ApplicationContext appContext;
    private final Engine engine;
    private final ExecutorService service;
    private final Logger log;

    public Foreman(ApplicationContext applicationContext, Engine engine, ExecutorService service, Logger log) {
        this.appContext = applicationContext;
        this.engine = engine;
        this.service = service;
        this.log = log;
    }

    // the foreman might take into consideration the average time required for a consumer to consume a task, the queue capacity
    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                boolean needToCreateProducer = needToCreateProducer();
                if (needToCreateProducer) {
                    createProducer();
                }
                boolean needToCreateConsumer = needToCreateConsumer();
                if (needToCreateConsumer) {
                    createConsumer();
                }
                if (!needToCreateConsumer && !needToCreateProducer) {
                    break;
                }
                Thread.sleep(5000);
            }
            log.info("Foreman stopped, no tasks left to execute!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean needToCreateConsumer() {
        return engine.blockingDeque.size() > 0;
    }

    private boolean needToCreateProducer() {
        int pendingTasks = engine.totalTasksToCreate.decrementAndGet();
        if (pendingTasks >= 0) {
            return true;
        } else {
            engine.totalTasksToCreate.compareAndSet(-1, 0);
            return false;
        }
    }

    private void createConsumer() {
        log.info("Creating new consumer!");
        Consumer consumer = appContext.getBean("consumer", Consumer.class);
        service.submit(consumer);
    }

    private void createProducer() {
        log.info("Creating new producer!");
        Producer producer = appContext.getBean("producer", Producer.class);
        service.submit(producer);
    }
}
