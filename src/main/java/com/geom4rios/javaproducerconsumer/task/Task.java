package com.geom4rios.javaproducerconsumer.task;

public interface Task {
    TaskType getTaskType();

    void execute();
}
