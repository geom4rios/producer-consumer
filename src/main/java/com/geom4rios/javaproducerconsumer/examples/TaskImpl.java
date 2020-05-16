package com.geom4rios.javaproducerconsumer.examples;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;

public class TaskImpl implements Task {

    @Override
    public void execute() {
        System.out.println("SOME IO INTENSIVE TASK");
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.IO_INTENSIVE;
    }
}