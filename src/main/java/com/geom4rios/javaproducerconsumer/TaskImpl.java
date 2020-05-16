package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.type.Task;
import com.geom4rios.javaproducerconsumer.type.TaskType;

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
