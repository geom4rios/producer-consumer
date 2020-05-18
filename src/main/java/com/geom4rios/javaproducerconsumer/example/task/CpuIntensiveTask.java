package com.geom4rios.javaproducerconsumer.example.task;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

public class CpuIntensiveTask implements Task {
    @Override
    public TaskType getTaskType() {
        return TaskType.CPU_INTENSIVE;
    }

    @Override
    public void execute() {
        // todo write a cpu intensive task
        System.out.println("CPU INTENSIVE TASK");
    }
}
