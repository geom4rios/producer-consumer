package com.geom4rios.javaproducerconsumer.type;

import java.util.List;

public interface Producer extends Runnable {
    List<Task> createNewTasks();

    int numberOfTasksToCreate();
}
