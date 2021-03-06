package com.geom4rios.javaproducerconsumer.example;

import com.geom4rios.javaproducerconsumer.example.task.MemoryIntensiveTaskImpl;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.task.Task;

import java.util.ArrayList;
import java.util.List;

public class MemoryProducer implements Producer {

    @Override
    public List<Task> createNewTasks() {
        List<Task> taskList = new ArrayList<>();
        int numberOfTasksToCreate = 40;
        for (int i = 0; i< numberOfTasksToCreate; i++) {
            taskList.add(new MemoryIntensiveTaskImpl());
        }
        return taskList;
    }
}
