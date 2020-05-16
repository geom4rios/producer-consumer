package com.geom4rios.javaproducerconsumer.type;

public interface Task {
    TaskType getTaskType();

    void execute();
}
