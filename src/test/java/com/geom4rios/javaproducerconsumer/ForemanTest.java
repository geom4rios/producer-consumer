package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.task.Task;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JavaProducerConsumerConfiguration.class})
@SpringBootTest(classes = {Logger.class, Engine.class, Foreman.class})
class ForemanTest {

    @Autowired
    Engine engine;

    @Autowired
    Foreman foreman;

    @Autowired
    Logger log;

    @Test
    @DisplayName("Test needToCreateConsumer based on how queues are populated")
    public void testNeedToCreateConsumer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Boolean shouldCreateConsumers;
        clearQueues();
        log.info("Test method will return false when all queues are empty");
        Method needToCreateConsumerReflected = getNeedToCreateConsumerReflected();
        shouldCreateConsumers = (Boolean) needToCreateConsumerReflected.invoke(foreman);
        assertThat(shouldCreateConsumers).isFalse();
        log.info("Test method will return true when shared queue is populated with at least one task");
        engine.concurrentLinkedDeque.add(getDummyTaskByType(TaskType.CPU_INTENSIVE));
        shouldCreateConsumers = (Boolean) needToCreateConsumerReflected.invoke(foreman);
        assertThat(shouldCreateConsumers).isTrue();
        clearQueues();
        log.info("Test method will return true when cpu queue is populated with at least one task");
        engine.cpuConcurrentLinkedDeque.add(getDummyTaskByType(TaskType.CPU_INTENSIVE));
        shouldCreateConsumers = (Boolean) needToCreateConsumerReflected.invoke(foreman);
        assertThat(shouldCreateConsumers).isTrue();
        clearQueues();
        log.info("Test method will return true when io queue populated with at least one task");
        engine.ioConcurrentLinkedDeque.add(getDummyTaskByType(TaskType.IO_INTENSIVE));
        shouldCreateConsumers = (Boolean) needToCreateConsumerReflected.invoke(foreman);
        assertThat(shouldCreateConsumers).isTrue();
        clearQueues();
        log.info("Test method will return true when memory queue is populated with at least one task");
        engine.memoryConcurrentLinkedDeque.add(getDummyTaskByType(TaskType.MEMORY_INTENSIVE));
        shouldCreateConsumers = (Boolean) needToCreateConsumerReflected.invoke(foreman);
        assertThat(shouldCreateConsumers).isTrue();
        clearQueues();
    }

    /* PRIVATE AUXILIARY */

    private Method getNeedToCreateConsumerReflected() throws NoSuchMethodException {
        Method needToCreateConsumer = Foreman.class.getDeclaredMethod("needToCreateConsumer");
        needToCreateConsumer.setAccessible(true);
        return needToCreateConsumer;
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

    private void clearQueues() {
        log.info("Clearing all queues");
        engine.concurrentLinkedDeque.clear();
        engine.cpuConcurrentLinkedDeque.clear();
        engine.ioConcurrentLinkedDeque.clear();
        engine.memoryConcurrentLinkedDeque.clear();
    }
}