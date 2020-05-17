package com.geom4rios.javaproducerconsumer.consumer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;

public class TaskDistributor implements Runnable {

    private final Engine engine;
    private final Logger log;

    public TaskDistributor(Engine engine, Logger log) {
        this.engine = engine;
        this.log = log;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = engine.concurrentLinkedDeque.pollLast();
                if (task != null) {
                    TaskType taskType = task.getTaskType();
                    switch (taskType) {
                        case IO_INTENSIVE:
                            log.debug("Adding task to io queue");
                            this.engine.ioConcurrentLinkedDeque.addLast(task);
                            break;
                        case CPU_INTENSIVE:
                            log.debug("Adding task to cpu queue");
                            this.engine.cpuConcurrentLinkedDeque.addLast(task);
                            break;
                        case MEMORY_INTENSIVE:
                            log.debug("Adding task to memory queue");
                            this.engine.memoryConcurrentLinkedDeque.addLast(task);
                            break;
                        default:
                            throw new UnsupportedOperationException("Task type not recognized!");
                    }
                } else {
                    break;
                }
            }
            log.info("Task distributor died after waiting for 1 second to poll an element from the queue and got nothing");
        } catch (Exception e) {
            log.info("Task distributor died after an exception occurred!");
            log.error(e.getMessage(), e);
        }
    }
}
