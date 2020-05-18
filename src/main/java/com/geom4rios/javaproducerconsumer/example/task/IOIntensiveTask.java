package com.geom4rios.javaproducerconsumer.example.task;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

public class IOIntensiveTask implements Task {

    @Override
    public void execute() {
        // todo write an io intensive task
        System.out.println("SOME IO INTENSIVE TASK");
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.IO_INTENSIVE;
    }
}
