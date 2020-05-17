package com.geom4rios.javaproducerconsumer.examples;

import com.geom4rios.javaproducerconsumer.examples.task.CpuIntensiveTask;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.task.Task;

import java.util.ArrayList;
import java.util.List;

public class CpuProducer implements Producer {

    private final int numberOfTasksToCreate = 2500;

    @Override
    public List<Task> createNewTasks() {
        List<Task> taskList = new ArrayList<>();
        for (int i=0; i<numberOfTasksToCreate; i++) {
            taskList.add(new CpuIntensiveTask());
        }
        return taskList;
    }
}
