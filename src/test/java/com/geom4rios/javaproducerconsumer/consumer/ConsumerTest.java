package com.geom4rios.javaproducerconsumer.consumer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ProducerConsumerTestConfiguration.class)
@SpringBootTest(classes = {Engine.class})
class ConsumerTest {

    @Autowired
    Engine engine;

    @Autowired
    Logger log;

    @BeforeEach
    public void setUp() {
        engine.concurrentLinkedDeque.clear();
        engine.ioConcurrentLinkedDeque.clear();
        engine.cpuConcurrentLinkedDeque.clear();
        engine.memoryConcurrentLinkedDeque.clear();
    }

    @Test
    @DisplayName("Test io consumer will only consume io tasks")
    public void testIOConsumer() {
        log.info("Create io consumer");
        Consumer ioConsumer = getConsumerByType(TaskType.IO_INTENSIVE);
        log.info("Add a task to the ioConcurrentLinkedDeque, cpuConcurrentLinkedDequeue, memoryConcurrentLinkedDequeue");
        engine.cpuConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.CPU_INTENSIVE));
        engine.ioConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.IO_INTENSIVE));
        engine.memoryConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.MEMORY_INTENSIVE));
        log.info("Run the consumer");
        ioConsumer.run();
        log.info("Assert that only io task is consumed");
        assertThat(engine.ioConcurrentLinkedDeque.size()).isZero();
        assertThat(engine.cpuConcurrentLinkedDeque.size()).isOne();
        assertThat(engine.memoryConcurrentLinkedDeque.size()).isOne();
    }

    @Test
    @DisplayName("Test cpu consumer will only consume cpu tasks")
    public void testCPUConsumer() {
        log.info("Create cpu consumer");
        Consumer ioConsumer = getConsumerByType(TaskType.CPU_INTENSIVE);
        log.info("Add a task to the ioConcurrentLinkedDeque, cpuConcurrentLinkedDequeue, memoryConcurrentLinkedDequeue");
        engine.cpuConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.CPU_INTENSIVE));
        engine.ioConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.IO_INTENSIVE));
        engine.memoryConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.MEMORY_INTENSIVE));
        log.info("Run the consumer");
        ioConsumer.run();
        log.info("Assert that only cpu task is consumed");
        assertThat(engine.ioConcurrentLinkedDeque.size()).isOne();
        assertThat(engine.cpuConcurrentLinkedDeque.size()).isZero();
        assertThat(engine.memoryConcurrentLinkedDeque.size()).isOne();
    }

    @Test
    @DisplayName("Test memory consumer will only consume memory tasks")
    public void testMemoryConsumer() {
        log.info("Create memory consumer");
        Consumer ioConsumer = getConsumerByType(TaskType.MEMORY_INTENSIVE);
        log.info("Add a task to the ioConcurrentLinkedDeque, cpuConcurrentLinkedDequeue, memoryConcurrentLinkedDequeue");
        engine.cpuConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.CPU_INTENSIVE));
        engine.ioConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.IO_INTENSIVE));
        engine.memoryConcurrentLinkedDeque.addFirst(getDummyTaskByType(TaskType.MEMORY_INTENSIVE));
        log.info("Run the consumer");
        ioConsumer.run();
        log.info("Assert that only memory task is consumed");
        assertThat(engine.ioConcurrentLinkedDeque.size()).isOne();
        assertThat(engine.cpuConcurrentLinkedDeque.size()).isOne();
        assertThat(engine.memoryConcurrentLinkedDeque.size()).isZero();
    }

    /* PRIVATE AUXILIARY */

    private Consumer getConsumerByType(TaskType taskType) {
        return new Consumer(engine, taskType, log);
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