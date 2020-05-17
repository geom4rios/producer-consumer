package com.geom4rios.javaproducerconsumer.example.task;

import com.geom4rios.javaproducerconsumer.task.MemoryIntensiveTask;
import com.geom4rios.javaproducerconsumer.task.TaskType;

public class MemoryIntensiveTaskImpl implements MemoryIntensiveTask {

    @Override
    public int memoryRequiredToRun() {
        return 512;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MEMORY_INTENSIVE;
    }

    @Override
    public void execute() {
        System.out.println("MEMORY INTENSIVE TASK");
    }
}
