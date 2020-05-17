package com.geom4rios.javaproducerconsumer.consumer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentLinkedDeque;

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
                if (taskType == TaskType.IO_INTENSIVE) {
                    if (!processTask(this.engine.ioConcurrentLinkedDeque)) {
                        break;
                    }
                } else if (taskType == TaskType.CPU_INTENSIVE) {
                    if (!processTask(this.engine.cpuConcurrentLinkedDeque)) {
                        break;
                    }
                } else if (taskType == TaskType.MEMORY_INTENSIVE) {
                    if (!processTask(this.engine.memoryConcurrentLinkedDeque)) {
                        break;
                    }
                } else {
                    throw new UnsupportedOperationException("Task type not recognized!");
                }
            }
            log.info("Consumer died after waiting for 1 second to poll an element from the queue and got nothing");
        } catch (Exception e) {
            log.info("Consumer died after an exception occured");
            log.error(e.getMessage(), e);
        }
        this.engine.decreaseConsumersRunningByType(taskType);
    }

    private boolean processTask(ConcurrentLinkedDeque<Task> queue) {
        Task task = queue.pollFirst();
        if (task != null) {
            log.info("Consuming " + this.taskType.name() + " task!");
            task.execute();
            this.engine.decreaseTaskByType(taskType);
            return true;
        } else {
            return false;
        }
    }
}
