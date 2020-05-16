package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.type.Task;
import com.geom4rios.javaproducerconsumer.type.TaskType;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {
    public static int numberOfProducersRunning = 0;
    public static int numberOfConsumersRunning = 0;
    public static int messagesProduced = 0;
    public static int messagesConsumed = 0;
    public AtomicInteger cpuIntensiveTasks = new AtomicInteger(0);
    public AtomicInteger ioIntensiveTasks = new AtomicInteger(0);
    public AtomicInteger totalTasksToCreate;
    private final int queueCapacity;
    private final TaskType taskType;
    public ConcurrentLinkedDeque<Task> blockingDeque;

    public Engine(int queueCapacity, TaskType taskType, int totalTasksToCreate) {
        this.queueCapacity = queueCapacity;
        this.taskType = taskType;
        blockingDeque = new ConcurrentLinkedDeque<>();
        this.totalTasksToCreate = new AtomicInteger(totalTasksToCreate);
    }
}
