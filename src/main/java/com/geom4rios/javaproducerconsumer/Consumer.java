package com.geom4rios.javaproducerconsumer;

import org.slf4j.Logger;

public class Consumer implements Runnable {

    private final Engine engine;
    private final Logger log;

    public Consumer(Engine engine, Logger log) {
        this.engine = engine;
        this.log = log;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = engine.blockingDeque.pollLast();
                if (task != null) {
                    log.info("Consuming task!");
                    task.execute();
                } else {
                    break;
                }
            }
            log.info("Consumer died after waiting for 1 second to poll an element from the queue and got null/nothing");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("Consumer died after receiving an exception");
        }
    }
}
