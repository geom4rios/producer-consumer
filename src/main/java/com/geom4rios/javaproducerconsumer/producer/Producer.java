package com.geom4rios.javaproducerconsumer.producer;

import com.geom4rios.javaproducerconsumer.task.Task;

import java.util.List;

public interface Producer {
    List<Task> createNewTasks();

    int numberOfTasksToCreate();
}
