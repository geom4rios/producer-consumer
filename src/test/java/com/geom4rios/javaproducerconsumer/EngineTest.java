package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.config.ProducerConsumerTestConfiguration;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ProducerConsumerTestConfiguration.class)
@ExtendWith(SpringExtension.class)
class EngineTest {

    @Autowired
    @Qualifier("engineWithPredefinedValues")
    Engine engine;

    @Autowired
    Logger log;

    @Test
    @DisplayName("Test engine decrease task by type works as expected for io")
    public void testEngineDecreaseTaskByTypeForIO() {
        // io
        int ioTasks = engine.ioIntensiveTasks.get();
        int ioTasksConsumed = engine.ioIntensiveTasksConsumed.get();
        // cpu
        int cpuTasks = engine.cpuIntensiveTasks.get();
        int cpuTasksConsumed = engine.cpuIntensiveTasksConsumed.get();
        // memory
        int memoryTasks = engine.memoryIntensiveTasks.get();
        int memoryTasksConsumed = engine.memoryIntensiveTasksConsumed.get();
        log.info("Invoking decreaseTaskByType method for io intensive task");
        engine.decreaseTaskByType(TaskType.IO_INTENSIVE);
        log.info("Assert that io tasks are decremented by one");
        assertThat(engine.ioIntensiveTasks.get()).isEqualTo(ioTasks - 1);
        log.info("Assert that io tasks consumed are incremented by one");
        assertThat(engine.ioIntensiveTasksConsumed.get()).isEqualTo(ioTasksConsumed + 1);
        log.info("Assert that other types of tasks are unchanged.");
        // cpu
        assertThat(cpuTasks).isEqualTo(engine.cpuIntensiveTasks.get());
        assertThat(cpuTasksConsumed).isEqualTo(engine.cpuIntensiveTasksConsumed.get());
        // memory
        assertThat(memoryTasks).isEqualTo(engine.memoryIntensiveTasks.get());
        assertThat(memoryTasksConsumed).isEqualTo(engine.memoryIntensiveTasksConsumed.get());
    }

    @Test
    @DisplayName("Test engine decrease task by type works as expected for memory")
    public void testEngineDecreaseTaskByTypeForMemory() {
        // io
        int ioTasks = engine.ioIntensiveTasks.get();
        int ioTasksConsumed = engine.ioIntensiveTasksConsumed.get();
        // cpu
        int cpuTasks = engine.cpuIntensiveTasks.get();
        int cpuTasksConsumed = engine.cpuIntensiveTasksConsumed.get();
        // memory
        int memoryTasks = engine.memoryIntensiveTasks.get();
        int memoryTasksConsumed = engine.memoryIntensiveTasksConsumed.get();
        log.info("Invoking decreaseTaskByType method for memory intensive task");
        engine.decreaseTaskByType(TaskType.MEMORY_INTENSIVE);
        log.info("Assert that memory tasks are decremented by one");
        assertThat(engine.memoryIntensiveTasks.get()).isEqualTo(memoryTasks - 1);
        log.info("Assert that memory tasks consumed are incremented by one");
        assertThat(engine.memoryIntensiveTasksConsumed.get()).isEqualTo(memoryTasksConsumed + 1);
        log.info("Assert that other types of tasks are unchanged.");
        // cpu
        assertThat(cpuTasks).isEqualTo(engine.cpuIntensiveTasks.get());
        assertThat(cpuTasksConsumed).isEqualTo(engine.cpuIntensiveTasksConsumed.get());
        // io
        assertThat(ioTasks).isEqualTo(engine.ioIntensiveTasks.get());
        assertThat(ioTasksConsumed).isEqualTo(engine.ioIntensiveTasksConsumed.get());
    }

    @Test
    @DisplayName("Test engine decrease task by type works as expected for cpu")
    public void testEngineDecreaseTaskByTypeForCPU() {
        // io
        int ioTasks = engine.ioIntensiveTasks.get();
        int ioTasksConsumed = engine.ioIntensiveTasksConsumed.get();
        // cpu
        int cpuTasks = engine.cpuIntensiveTasks.get();
        int cpuTasksConsumed = engine.cpuIntensiveTasksConsumed.get();
        // memory
        int memoryTasks = engine.memoryIntensiveTasks.get();
        int memoryTasksConsumed = engine.memoryIntensiveTasksConsumed.get();

        engine.decreaseTaskByType(TaskType.CPU_INTENSIVE);
        log.info("Assert that cpu tasks are decremented by one");
        assertThat(engine.cpuIntensiveTasks.get()).isEqualTo(cpuTasks - 1);
        log.info("Assert that cpu tasks consumed are incremented by one");
        assertThat(engine.cpuIntensiveTasksConsumed.get()).isEqualTo(cpuTasksConsumed + 1);
        log.info("Assert that other types of tasks are unchanged.");
        // io
        assertThat(ioTasks).isEqualTo(engine.ioIntensiveTasks.get());
        assertThat(ioTasksConsumed).isEqualTo(engine.ioIntensiveTasksConsumed.get());
        // memory
        assertThat(memoryTasks).isEqualTo(engine.memoryIntensiveTasks.get());
        assertThat(memoryTasksConsumed).isEqualTo(engine.memoryIntensiveTasksConsumed.get());
    }

    @Test
    @DisplayName("Test engine DecreaseConsumerRunningByType works as expected for io tasks")
    public void testEngineDecreaseConsumerByTypeForIO() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.decreaseConsumersRunningByType(TaskType.IO_INTENSIVE);
        log.info("Assert that io consumers running are decreased by one");
        assertThat(engine.ioIntensiveConsumersRunning.get()).isEqualTo(ioConsumers - 1);
        log.info("Assert that total number of consumers is decreased by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers - 1);
        log.info("Assert that other consumers running are not affected");
        // cpu
        assertThat(cpuConsumers).isEqualTo(engine.cpuIntensiveConsumersRunning.get());
        // memory
        assertThat(memoryConsumers).isEqualTo(engine.memoryIntensiveConsumersRunning.get());
    }

    @Test
    @DisplayName("Test engine DecreaseConsumerRunningByType works as expected for cpu tasks")
    public void testEngineDecreaseConsumerByTypeForCPU() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.decreaseConsumersRunningByType(TaskType.CPU_INTENSIVE);
        log.info("Assert that cpu consumers running are decreased by one");
        assertThat(engine.cpuIntensiveConsumersRunning.get()).isEqualTo(cpuConsumers - 1);
        log.info("Assert that total number of consumers is decreased by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers - 1);
        log.info("Assert that other consumers running are not affected");
        // io
        assertThat(ioConsumers).isEqualTo(engine.ioIntensiveConsumersRunning.get());
        // memory
        assertThat(memoryConsumers).isEqualTo(engine.memoryIntensiveConsumersRunning.get());
    }

    @Test
    @DisplayName("Test engine DecreaseConsumerRunningByType works as expected for memory tasks")
    public void testEngineDecreaseConsumerByTypeForMemory() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.decreaseConsumersRunningByType(TaskType.MEMORY_INTENSIVE);
        log.info("Assert that memory consumers running are decreased by one");
        assertThat(engine.memoryIntensiveConsumersRunning.get()).isEqualTo(memoryConsumers - 1);
        log.info("Assert that total number of consumers is decreased by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers - 1);
        log.info("Assert that other consumers running are not affected");
        // io
        assertThat(ioConsumers).isEqualTo(engine.ioIntensiveConsumersRunning.get());
        // cpu
        assertThat(cpuConsumers).isEqualTo(engine.cpuIntensiveConsumersRunning.get());
    }

    @Test
    @DisplayName("Test engine IncreaseConsumersRunningByType works as expected for io tasks")
    public void testEngineIncreaseConsumerByTypeForIO() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.increaseConsumersRunningByType(TaskType.IO_INTENSIVE);
        log.info("Assert that io consumers running are decreased by one");
        assertThat(engine.ioIntensiveConsumersRunning.get()).isEqualTo(ioConsumers + 1);
        log.info("Assert that total number of consumers is decreased by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers + 1);
        log.info("Assert that other consumers running are not affected");
        // cpu
        assertThat(cpuConsumers).isEqualTo(engine.cpuIntensiveConsumersRunning.get());
        // memory
        assertThat(memoryConsumers).isEqualTo(engine.memoryIntensiveConsumersRunning.get());
    }

    @Test
    @DisplayName("Test engine IncreaseConsumersRunningByType works as expected for cpu tasks")
    public void testEngineIncreaseConsumerByTypeForCPU() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.increaseConsumersRunningByType(TaskType.CPU_INTENSIVE);
        log.info("Assert that cpu consumers running are increased by one");
        assertThat(engine.cpuIntensiveConsumersRunning.get()).isEqualTo(cpuConsumers + 1);
        log.info("Assert that total number of consumers is increased by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers + 1);
        log.info("Assert that other consumers running are not affected");
        // io
        assertThat(ioConsumers).isEqualTo(engine.ioIntensiveConsumersRunning.get());
        // memory
        assertThat(memoryConsumers).isEqualTo(engine.memoryIntensiveConsumersRunning.get());
    }

    @Test
    @DisplayName("Test engine IncreaseConsumersRunningByType works as expected for memory tasks")
    public void testEngineIncreaseConsumerByTypeForMemory() {
        int ioConsumers = engine.ioIntensiveConsumersRunning.get();
        int cpuConsumers = engine.cpuIntensiveConsumersRunning.get();
        int memoryConsumers = engine.memoryIntensiveConsumersRunning.get();
        int totalConsumers = engine.numberOfConsumersRunning.get();
        engine.increaseConsumersRunningByType(TaskType.MEMORY_INTENSIVE);
        log.info("Assert that memory consumers running are increase by one");
        assertThat(engine.memoryIntensiveConsumersRunning.get()).isEqualTo(memoryConsumers + 1);
        log.info("Assert that total number of consumers is increase by one");
        assertThat(engine.numberOfConsumersRunning.get()).isEqualTo(totalConsumers + 1);
        log.info("Assert that other consumers running are not affected");
        // io
        assertThat(ioConsumers).isEqualTo(engine.ioIntensiveConsumersRunning.get());
        // cpu
        assertThat(cpuConsumers).isEqualTo(engine.cpuIntensiveConsumersRunning.get());
    }

}