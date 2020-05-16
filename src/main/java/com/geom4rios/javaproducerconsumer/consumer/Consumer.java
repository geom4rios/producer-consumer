package com.geom4rios.javaproducerconsumer.consumer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;

public class Consumer implements Runnable {

    private final Engine engine;
    private final TaskType taskType;
    private final Logger log;

    public Consumer(Engine engine, TaskType taskType, Logger log) {
        this.engine = engine;
        this.taskType = taskType;
        this.log = log;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = engine.concurrentLinkedDeque.pollLast();
                if (task != null) {
                    if (task.getTaskType() == this.taskType) {
                        log.info("Consuming " + this.taskType.name() + " task!");
                        task.execute();
                        this.engine.decreaseTaskByType(taskType);
                    } else {
                        log.info("Adding task back to queue");
                        engine.concurrentLinkedDeque.addFirst(task);
                        Thread.sleep(500); // leave some time for other consumers to pick that task
                    }
                } else {
                    break;
                }
            }
            log.info("Consumer died after waiting for 1 second to poll an element from the queue and got nothing");
        } catch (Exception e) {
            log.info("Consumer died after an exception occured");
            log.error(e.getMessage(), e);
        }
        this.engine.decreaseConsumersRunningByType(taskType);
    }
}
