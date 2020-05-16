package com.geom4rios.javaproducerconsumer.examples;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

public class CpuIntensiveTask implements Task {
    @Override
    public TaskType getTaskType() {
        return TaskType.CPU_INTENSIVE;
    }

    @Override
    public void execute() {
        System.out.println("CPU INTENSIVE TASK");
    }
}
