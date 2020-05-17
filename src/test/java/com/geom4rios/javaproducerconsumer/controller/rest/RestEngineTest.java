package com.geom4rios.javaproducerconsumer.controller.rest;

import com.geom4rios.javaproducerconsumer.Engine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class RestEngineTest {

    private int expectedProducersRunning = 10;
    private int expectedConsumersRunning = 20;
    private int expectedCpuConsumersRunning = 30;
    private int expectedIOConsumersRunning = 40;
    private int expectedMemoryConsumersRunning = 50;
    private int expectedCpuTasksConsumed = 60;
    private int expectedIOTasksConsumed = 70;
    private int expectedMemoryTasksConsumed = 80;
    private int expectedPendingCpuTasks = 90;
    private int expectedPendingIOTasks = 100;
    private int expectedPendingMemoryTasks = 110;

    @Test
    @DisplayName("Tests conversion from engine to rest engine.")
    public void testRestEngineConversion() {
        Engine engine = getEngineWithPredefinedValues();
        RestEngine restEngine = new RestEngine(engine);

        // producer
        assertThat(restEngine.getConsumersRunning()).isEqualTo(engine.numberOfConsumersRunning.get());
        // consumers
        assertThat(restEngine.getProducersRunning()).isEqualTo(engine.numberOfProducersRunning.get());
        assertThat(restEngine.getCpuConsumersRunning()).isEqualTo(engine.cpuIntensiveConsumersRunning.get());
        assertThat(restEngine.getIoConsumersRunning()).isEqualTo(engine.ioIntensiveConsumersRunning.get());
        assertThat(restEngine.getMemoryConsumersRunning()).isEqualTo(engine.memoryIntensiveConsumersRunning.get());
        // consumed
        assertThat(restEngine.getCpuIntensiveTasksConsumed()).isEqualTo(engine.cpuIntensiveTasksConsumed.get());
        assertThat(restEngine.getIoIntensiveTasksConsumed()).isEqualTo(engine.ioIntensiveTasksConsumed.get());
        assertThat(restEngine.getMemoryIntensiveTasksConsumed()).isEqualTo(engine.memoryIntensiveTasksConsumed.get());
        // pending
        assertThat(restEngine.getPendingCpuIntensiveTasks()).isEqualTo(engine.cpuIntensiveTasks.get());
        assertThat(restEngine.getPendingIOIntensiveTasks()).isEqualTo(engine.ioIntensiveTasks.get());
        assertThat(restEngine.getPendingMemoryIntensiveTasks()).isEqualTo(engine.memoryIntensiveTasks.get());
    }

    private Engine getEngineWithPredefinedValues() {
        Engine engine = new Engine();
        engine.numberOfProducersRunning.compareAndSet(0, expectedProducersRunning);
        engine.numberOfConsumersRunning.compareAndSet(0, expectedConsumersRunning);
        engine.cpuIntensiveConsumersRunning.compareAndSet(0, expectedCpuConsumersRunning);
        engine.ioIntensiveConsumersRunning.compareAndSet(0, expectedIOConsumersRunning);
        engine.memoryIntensiveConsumersRunning.compareAndSet(0, expectedMemoryConsumersRunning);
        engine.cpuIntensiveTasksConsumed.compareAndSet(0, expectedCpuTasksConsumed);
        engine.ioIntensiveTasksConsumed.compareAndSet(0, expectedIOTasksConsumed);
        engine.memoryIntensiveTasksConsumed.compareAndSet(0, expectedMemoryTasksConsumed);
        engine.cpuIntensiveTasks.compareAndSet(0, expectedPendingCpuTasks);
        engine.ioIntensiveTasks.compareAndSet(0, expectedPendingIOTasks);
        engine.memoryIntensiveTasks.compareAndSet(0, expectedPendingMemoryTasks);
        return engine;
    }

}