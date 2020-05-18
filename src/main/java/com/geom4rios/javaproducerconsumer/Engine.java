package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that holds the state of the producer-consumer application, it holds the common queue as well as the task type specific queues & various monitor statistics based on which the foreman can make decisions.
 */
public class Engine {
    // producers
    public AtomicInteger numberOfProducersRunning = new AtomicInteger(0);
    // consumers
    public AtomicInteger numberOfConsumersRunning = new AtomicInteger(0);
    public AtomicInteger cpuIntensiveConsumersRunning = new AtomicInteger(0);
    public AtomicInteger ioIntensiveConsumersRunning = new AtomicInteger(0);
    public AtomicInteger memoryIntensiveConsumersRunning = new AtomicInteger(0);
    // consumed
    public AtomicInteger cpuIntensiveTasksConsumed = new AtomicInteger(0);
    public AtomicInteger ioIntensiveTasksConsumed = new AtomicInteger(0);
    public AtomicInteger memoryIntensiveTasksConsumed = new AtomicInteger(0);
    // pending
    public AtomicInteger cpuIntensiveTasks = new AtomicInteger(0);
    public AtomicInteger ioIntensiveTasks = new AtomicInteger(0);
    public AtomicInteger memoryIntensiveTasks = new AtomicInteger(0);
    // shared queue
    public ConcurrentLinkedDeque<Task> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();
    // specific queues per task type
    public ConcurrentLinkedDeque<Task> cpuConcurrentLinkedDeque = new ConcurrentLinkedDeque<>();
    public ConcurrentLinkedDeque<Task> ioConcurrentLinkedDeque = new ConcurrentLinkedDeque<>();
    public ConcurrentLinkedDeque<Task> memoryConcurrentLinkedDeque = new ConcurrentLinkedDeque<>();


    /**
     * Decrements the number of the pending tasks based on the {@link TaskType taskType} provided. <br>
     * Increments the tasks consumed based on the {@link TaskType taskType} provided.
     *
     * @param taskType the {@link TaskType taskType}
     */
    public void decreaseTaskByType(TaskType taskType) {
        switch (taskType) {
            case IO_INTENSIVE:
                this.ioIntensiveTasks.decrementAndGet();
                this.ioIntensiveTasksConsumed.incrementAndGet();
                break;
            case CPU_INTENSIVE:
                this.cpuIntensiveTasks.decrementAndGet();
                this.cpuIntensiveTasksConsumed.incrementAndGet();
                break;
            case MEMORY_INTENSIVE:
                this.memoryIntensiveTasks.decrementAndGet();
                this.memoryIntensiveTasksConsumed.incrementAndGet();
                break;
            default:
                throw new UnsupportedOperationException("Task type not recognized!");
        }
    }

    /**
     * Decreases the consumers running by the {@link TaskType taskType} provided. <br>
     * Also decreases the total consumers running.
     *
     * @param taskType the {@link TaskType taskType}
     */
    public void decreaseConsumersRunningByType(TaskType taskType) {
        switch(taskType) {
            case IO_INTENSIVE:
                this.ioIntensiveConsumersRunning.decrementAndGet();
                this.numberOfConsumersRunning.decrementAndGet();
                break;
            case CPU_INTENSIVE:
                this.cpuIntensiveConsumersRunning.decrementAndGet();
                this.numberOfConsumersRunning.decrementAndGet();
                break;
            case MEMORY_INTENSIVE:
                this.memoryIntensiveConsumersRunning.decrementAndGet();
                this.numberOfConsumersRunning.decrementAndGet();
                break;
            default:
                throw new UnsupportedOperationException("Task type not recognized!");
        }
    }

    /**
     * Increases the consumers running based on the {@link TaskType taskType} provided. <br>
     * Increases the total consumers running <br>
     *
     * @param taskType the {@link TaskType taskType}
     */
    public void increaseConsumersRunningByType(TaskType taskType) {
        switch(taskType) {
            case IO_INTENSIVE:
                this.ioIntensiveConsumersRunning.incrementAndGet();
                this.numberOfConsumersRunning.incrementAndGet();
                break;
            case CPU_INTENSIVE:
                this.cpuIntensiveConsumersRunning.incrementAndGet();
                this.numberOfConsumersRunning.incrementAndGet();
                break;
            case MEMORY_INTENSIVE:
                this.memoryIntensiveConsumersRunning.incrementAndGet();
                this.numberOfConsumersRunning.incrementAndGet();
                break;
            default:
                throw new UnsupportedOperationException("Task type not recognized!");
        }
    }
}
