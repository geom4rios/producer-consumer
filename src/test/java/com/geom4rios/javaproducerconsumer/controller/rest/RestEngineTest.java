package com.geom4rios.javaproducerconsumer.controller.rest;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ProducerConsumerTestConfiguration.class)
@ExtendWith(SpringExtension.class)
class RestEngineTest {

    @Autowired
    @Qualifier("engineWithPredefinedValues")
    Engine engine;

    @Test
    @DisplayName("Tests conversion from engine to rest engine.")
    public void testRestEngineConversion() {
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
}