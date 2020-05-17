package com.geom4rios.javaproducerconsumer.config;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.example.task.CpuIntensiveTask;
import com.geom4rios.javaproducerconsumer.example.task.IOIntensiveTask;
import com.geom4rios.javaproducerconsumer.example.task.MemoryIntensiveTaskImpl;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import com.geom4rios.javaproducerconsumer.task.MemoryIntensiveTask;
import com.geom4rios.javaproducerconsumer.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class ProducerConsumerTestConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint injectionPoint){

        if (injectionPoint.getMethodParameter() != null) {
            return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass().getName());
        } else if (injectionPoint.getMember() != null) {
            return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        } else {
            return LoggerFactory.getLogger(this.getClass().getName());
        }
    }

    @Bean(name = "ioProducer")
    public Producer getIoProducer() {
        return new Producer() {
            @Override
            public List<Task> createNewTasks() {
                List<Task> taskList = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    taskList.add(new IOIntensiveTask());
                }
                return taskList;
            }
        };
    }

    @Bean(name = "cpuProducer")
    public Producer getCpuProducer() {
        return new Producer() {
            @Override
            public List<Task> createNewTasks() {
                List<Task> taskList = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    taskList.add(new CpuIntensiveTask());
                }
                return taskList;
            }
        };
    }

    @Bean(name = "memoryProducer")
    public Producer getMemoryProducer() {
        return new Producer() {
            @Override
            public List<Task> createNewTasks() {
                List<Task> taskList = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    taskList.add(new MemoryIntensiveTaskImpl());
                }
                return taskList;
            }
        };
    }

    @Bean(name = "engineWithPredefinedValues")
    public Engine getEngineWithPredefinedValues() {
        Engine engine = new Engine();
        int expectedProducersRunning = 10;
        engine.numberOfProducersRunning.compareAndSet(0, expectedProducersRunning);
        int expectedConsumersRunning = 20;
        engine.numberOfConsumersRunning.compareAndSet(0, expectedConsumersRunning);
        int expectedCpuConsumersRunning = 30;
        engine.cpuIntensiveConsumersRunning.compareAndSet(0, expectedCpuConsumersRunning);
        int expectedIOConsumersRunning = 40;
        engine.ioIntensiveConsumersRunning.compareAndSet(0, expectedIOConsumersRunning);
        int expectedMemoryConsumersRunning = 50;
        engine.memoryIntensiveConsumersRunning.compareAndSet(0, expectedMemoryConsumersRunning);
        int expectedCpuTasksConsumed = 60;
        engine.cpuIntensiveTasksConsumed.compareAndSet(0, expectedCpuTasksConsumed);
        int expectedIOTasksConsumed = 70;
        engine.ioIntensiveTasksConsumed.compareAndSet(0, expectedIOTasksConsumed);
        int expectedMemoryTasksConsumed = 80;
        engine.memoryIntensiveTasksConsumed.compareAndSet(0, expectedMemoryTasksConsumed);
        int expectedPendingCpuTasks = 90;
        engine.cpuIntensiveTasks.compareAndSet(0, expectedPendingCpuTasks);
        int expectedPendingIOTasks = 100;
        engine.ioIntensiveTasks.compareAndSet(0, expectedPendingIOTasks);
        int expectedPendingMemoryTasks = 110;
        engine.memoryIntensiveTasks.compareAndSet(0, expectedPendingMemoryTasks);
        return engine;
    }
}
