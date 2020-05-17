package com.geom4rios.javaproducerconsumer.consumer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(ProducerConsumerTestConfiguration.class)
@SpringBootTest(classes = {Logger.class, Engine.class, ConsumerDistributor.class})
class ConsumerDistributorTest {

    @Autowired
    Engine engine;

    @Autowired
    Logger log;

    @Autowired
    ConsumerDistributor consumerDistributor;

    @BeforeEach
    public void setUp() {
        engine.concurrentLinkedDeque.clear();
        engine.ioConcurrentLinkedDeque.clear();
        engine.cpuConcurrentLinkedDeque.clear();
        engine.memoryConcurrentLinkedDeque.clear();
    }

    @Test
    @DisplayName("IO Intensive task goes to io queue only")
    public void testConsumerDistributorForIOTasks() {
        Task task = getDummyTaskByType(TaskType.IO_INTENSIVE);
        log.info("Insert io task into the shared queue");
        engine.concurrentLinkedDeque.addLast(task);
        log.info("Invoke method");
        consumerDistributor.run();
        log.info("assert that ioQueue got the task");
        assertThat(engine.ioConcurrentLinkedDeque.size()).as("IO concurrent queue should got the task after the distributor run").isOne();
        log.info("Assert that rest of the queues are empty");
        verifyQueuesAreEmpty(engine.cpuConcurrentLinkedDeque, engine.memoryConcurrentLinkedDeque);
    }

    @Test
    @DisplayName("CPU Intensive task goes to cpu queue only")
    public void testConsumerDistributorForCpuTasks() {
        Task task = getDummyTaskByType(TaskType.CPU_INTENSIVE);
        log.info("Insert cpu task into the shared queue");
        engine.concurrentLinkedDeque.addLast(task);
        log.info("Invoke method");
        consumerDistributor.run();
        log.info("assert that cpuQueue got the task");
        assertThat(engine.cpuConcurrentLinkedDeque.size()).as("CPU concurrent queue should got the task after the distributor run").isOne();
        log.info("Assert that rest of the queues are empty");
        verifyQueuesAreEmpty(engine.ioConcurrentLinkedDeque, engine.memoryConcurrentLinkedDeque);
    }

    // test when shard queue has a memory intensive task that the distributor will poll and put the task into the memory queue
    @Test
    @DisplayName("Memory Intensive task goes to memory queue only")
    public void testConsumerDistributorForMemoryTasks() {
        Task task = getDummyTaskByType(TaskType.MEMORY_INTENSIVE);
        log.info("Insert memory task into the shared queue");
        engine.concurrentLinkedDeque.addLast(task);
        log.info("Invoke method");
        consumerDistributor.run();
        log.info("assert that memoryQueue got the task");
        assertThat(engine.memoryConcurrentLinkedDeque.size()).as("Memory concurrent queue should got the task after the distributor run").isOne();
        log.info("Assert that rest of the queues are empty");
        verifyQueuesAreEmpty(engine.ioConcurrentLinkedDeque, engine.cpuConcurrentLinkedDeque);
    }

    /* PRIVATE AUXILIARY */

    private void verifyQueuesAreEmpty(Queue... queues) {
        for (Queue queue : queues) {
            assertThat(queue.size()).isZero();
        }
    }

    private Task getDummyTaskByType(TaskType taskType) {
        return new Task() {
            @Override
            public TaskType getTaskType() {
                return taskType;
            }
            @Override
            public void execute() {
                System.out.println(taskType.name() + " TASK");
            }
        };
    }
}