package com.geom4rios.javaproducerconsumer.example.task;

import com.geom4rios.javaproducerconsumer.task.MemoryIntensiveTask;

public class MemoryIntensiveTaskImpl implements MemoryIntensiveTask {

    @Override
    public int memoryRequiredToRun() {
        return 512;
    }

    @Override
    public void execute() {
        // todo write a memory intensive task
        System.out.println("MEMORY INTENSIVE TASK");
    }
}
