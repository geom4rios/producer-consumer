package com.geom4rios.javaproducerconsumer.controller.rest;

import com.geom4rios.javaproducerconsumer.Engine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class RestEngine {
    private int producersRunning;
    // consumers
    private int consumersRunning;
    private int cpuConsumersRunning;
    private int ioConsumersRunning;
    private int memoryConsumersRunning;
    // consumed tasks
    private int cpuIntensiveTasksConsumed;
    private int memoryIntensiveTasksConsumed;
    private int ioIntensiveTasksConsumed;
    // pending tasks
    private int pendingCpuIntensiveTasks;
    private int pendingIOIntensiveTasks;
    private int pendingMemoryIntensiveTasks;
    // total tasks
    private int totalTasksProduced;
    private int totalTasksConsumed;
    private int totalPendingTasks;
    // system
    private int threadsRunning;
    private long memoryAllocated;

    public RestEngine(Engine engine) {
        this.producersRunning = engine.numberOfProducersRunning.get();
        // consumers
        this.consumersRunning = engine.numberOfConsumersRunning.get();
        this.cpuConsumersRunning = engine.cpuIntensiveConsumersRunning.get();
        this.ioConsumersRunning = engine.ioIntensiveConsumersRunning.get();
        this.memoryConsumersRunning = engine.memoryIntensiveConsumersRunning.get();
        // set consumed tasks
        this.cpuIntensiveTasksConsumed = engine.cpuIntensiveTasksConsumed.get();
        this.ioIntensiveTasksConsumed = engine.ioIntensiveTasksConsumed.get();
        this.memoryIntensiveTasksConsumed = engine.memoryIntensiveTasksConsumed.get();
        // set pending tasks
        this.pendingCpuIntensiveTasks = engine.cpuIntensiveTasks.get();
        this.pendingIOIntensiveTasks = engine.ioIntensiveTasks.get();
        this.pendingMemoryIntensiveTasks = engine.memoryIntensiveTasks.get();
        // set total tasks
        this.totalTasksConsumed = this.cpuIntensiveTasksConsumed + this.ioIntensiveTasksConsumed + memoryIntensiveTasksConsumed;
        this.totalPendingTasks = this.pendingCpuIntensiveTasks + this.pendingIOIntensiveTasks + this.pendingMemoryIntensiveTasks;
        this.totalTasksProduced = this.totalPendingTasks + this.totalTasksConsumed;
        // set system metrics
        this.threadsRunning = Thread.getAllStackTraces().keySet().size();
        this.memoryAllocated = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
