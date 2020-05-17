package com.geom4rios.javaproducerconsumer.producer;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import com.geom4rios.javaproducerconsumer.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ProducerConsumerTestConfiguration.class)
@SpringBootTest(classes = {Logger.class, Engine.class})
class ProducerRunnerTest {

    @Autowired
    Engine engine;

    @Autowired
    Logger log;

    @Autowired
    @Qualifier("ioProducer")
    Producer ioProducer;

    @Autowired
    @Qualifier("cpuProducer")
    Producer cpuProducer;

    @Autowired
    @Qualifier("memoryProducer")
    Producer memoryProducer;

    @BeforeEach
    public void setUp() {
        engine.concurrentLinkedDeque.clear();
    }

    @Test
    @DisplayName("Test ProducerRunner for io tasks")
    public void testProducerRunnerForIOTasks() {
        List<Task> expectedTasks = ioProducer.createNewTasks();
        int expectedTasksWrittenOnSharedQueue = expectedTasks.size();
        log.info("Create the IO ProducerRunner");
        ProducerRunner ioProducerRunner = new ProducerRunner(engine, ioProducer, log);
        log.info("Run ProducerRunner with a producer containing only IO tasks");
        ioProducerRunner.run();
        log.info("Assert that the producer tasks are written correctly into the shared queue");
        assertThat(engine.concurrentLinkedDeque.size()).isEqualTo(expectedTasksWrittenOnSharedQueue);
        engine.concurrentLinkedDeque.forEach(t -> assertThat(expectedTasks.contains(t)));
    }

    @Test
    @DisplayName("Test ProducerRunner for cpu tasks")
    public void testProducerRunnerForCpuTasks() {
        List<Task> expectedTasks = cpuProducer.createNewTasks();
        int expectedTasksWrittenOnSharedQueue = expectedTasks.size();
        log.info("Create the CPU ProducerRunner");
        ProducerRunner cpuProducerRunner = new ProducerRunner(engine, cpuProducer, log);
        log.info("Run ProducerRunner with a producer containing only CPU tasks");
        cpuProducerRunner.run();
        log.info("Assert that the producer tasks are written correctly into the shared queue");
        assertThat(engine.concurrentLinkedDeque.size()).isEqualTo(expectedTasksWrittenOnSharedQueue);
        engine.concurrentLinkedDeque.forEach(t -> assertThat(expectedTasks.contains(t)));
    }

    @Test
    @DisplayName("Test ProducerRunner for memory tasks")
    public void testProducerRunnerForMemoryTasks() {
        List<Task> expectedTasks = memoryProducer.createNewTasks();
        int expectedTasksWrittenOnSharedQueue = expectedTasks.size();
        log.info("Create the Memory ProducerRunner");
        ProducerRunner memoryProducerRunner = new ProducerRunner(engine, memoryProducer, log);
        log.info("Run ProducerRunner with a producer containing only Memory tasks");
        memoryProducerRunner.run();
        log.info("Assert that the producer tasks are written correctly into the shared queue");
        assertThat(engine.concurrentLinkedDeque.size()).isEqualTo(expectedTasksWrittenOnSharedQueue);
        engine.concurrentLinkedDeque.forEach(t -> assertThat(expectedTasks.contains(t)));
    }

}