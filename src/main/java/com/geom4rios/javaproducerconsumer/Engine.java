package com.geom4rios.javaproducerconsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {
    public static int numberOfProducersRunning = 0;
    public static int numberOfConsumersRunning = 0;
    public static int messagesProduced = 0;
    public static int messagesConsumed = 0;
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
