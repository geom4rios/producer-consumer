package com.geom4rios.javaproducerconsumer;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Producer implements Runnable {

    private final Engine engine;
    private final Logger log;

    public Producer(Engine engine, Logger log) {
        this.engine = engine;
        this.log = log;
    }

    @Override
    public void run() {
        while (true) {
            int pendingTasksNumber = this.engine.totalTasksToCreate.decrementAndGet();
            if (pendingTasksNumber > 0) {
                log.info("Creating new task");
                // create new task
                Task task = new Task();
                // write to queue
                this.engine.blockingDeque.addLast(task);
            } else {
                break;
            }
        }
        log.info("Producer completed!");
    }
}
