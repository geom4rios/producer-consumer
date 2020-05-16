package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

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
    // queue
    public ConcurrentLinkedDeque<Task> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();

    public void decreaseTaskByType(TaskType taskType) {
        switch(taskType) {
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
}
