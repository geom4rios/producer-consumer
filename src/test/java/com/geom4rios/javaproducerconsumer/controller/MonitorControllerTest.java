package com.geom4rios.javaproducerconsumer.controller;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import com.geom4rios.javaproducerconsumer.controller.rest.RestEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ProducerConsumerTestConfiguration.class)
@SpringBootTest(classes = {Logger.class, MonitorController.class})
class MonitorControllerTest {

    @Autowired
    @Qualifier("engineWithPredefinedValues")
    Engine engine;

    @Autowired
    MonitorController monitorController;

    // test web mvc json response based on engine current values
    @Test
    @DisplayName("Test MonitorController returns expected RestEngine")
    public void testMonitorControllerResponse() {
        RestEngine restEngine = monitorController.getRestEngine();
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
}