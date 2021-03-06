package com.geom4rios.javaproducerconsumer.producer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;

import java.util.List;

/**
 * Each instance of this class holds one {@link Producer} <br>
 * Instances of this class will get the tasks that a producer is holding and add them to the shared queue.
 */
public class ProducerRunner implements Runnable {

    private final Engine engine;
    private final Producer producer;
    private final Logger log;

    public ProducerRunner(Engine engine, Producer producer, Logger log) {
        this.engine = engine;
        this.producer = producer;
        this.log = log;
    }

    @Override
    public void run() {
        this.engine.numberOfProducersRunning.incrementAndGet();
        List<Task> newTasks = this.producer.createNewTasks();
        for (Task taskCreated : newTasks) {
            this.engine.concurrentLinkedDeque.addLast(taskCreated);
            if (taskCreated.getTaskType() == TaskType.IO_INTENSIVE) {
                this.engine.ioIntensiveTasks.addAndGet(1);
            } else if (taskCreated.getTaskType() == TaskType.CPU_INTENSIVE) {
                this.engine.cpuIntensiveTasks.addAndGet(1);
            } else if (taskCreated.getTaskType() == TaskType.MEMORY_INTENSIVE) {
                this.engine.memoryIntensiveTasks.addAndGet(1);
            }
            log.debug("Task produced of type: " + taskCreated.getTaskType().name());
        }
        log.info("Producer completed! tasks produced: " + newTasks.size());
        this.engine.numberOfProducersRunning.decrementAndGet();
    }
}
