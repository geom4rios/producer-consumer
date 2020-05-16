package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.type.Producer;
import com.geom4rios.javaproducerconsumer.type.Task;
import com.geom4rios.javaproducerconsumer.type.TaskType;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ProducerImpl implements Producer {

    private final Engine engine;
    private final Logger log;

    public ProducerImpl(Engine engine, Logger log) {
        this.engine = engine;
        this.log = log;
    }

    @Override
    public void run() {
        List<Task> newTasks = createNewTasks();
        for (Task taskCreated : newTasks) {
            this.engine.blockingDeque.addLast(taskCreated);
            if (taskCreated.getTaskType() == TaskType.IO_INTENSIVE) {
                this.engine.ioIntensiveTasks.addAndGet(1);
            } else if (taskCreated.getTaskType() == TaskType.CPU_INTENSIVE) {
                this.engine.cpuIntensiveTasks.addAndGet(1);
            }
            log.info("Task produced of type: " + taskCreated.getTaskType().name());
        }
        log.info("Producer completed!");
    }

    @Override
    public List<Task> createNewTasks() {
        List<Task> taskList = new ArrayList<>();
        for (int i=0; i<numberOfTasksToCreate(); i++) {
            taskList.add(new TaskImpl());
        }
        return taskList;
    }

    @Override
    public int numberOfTasksToCreate() {
        return 10;
    }
}
