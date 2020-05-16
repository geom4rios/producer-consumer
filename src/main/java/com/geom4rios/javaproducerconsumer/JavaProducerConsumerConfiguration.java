package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.type.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class JavaProducerConsumerConfiguration {

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(JavaProducerConsumerApplication.class);
    }

    @Bean(name = "producerExecutor")
    public ExecutorService getProducerExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean(name = "cpuIntensiveExecutor")
    public ExecutorService getCpuIntensiveExecutor() {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        if (numberOfCores > 1) {
            int maxThreadsForCpuIntensiveTasks = numberOfCores % 2 == 0 ? numberOfCores/2 : (numberOfCores - 1)/2;
            return Executors.newFixedThreadPool(maxThreadsForCpuIntensiveTasks);
        } else {
            return Executors.newSingleThreadExecutor();
        }
    }

    @Bean(name = "ioIntensiveExecutor")
    public ExecutorService getExecutor() {
        return Executors.newFixedThreadPool(16);
    }

    @Bean
    public Engine getEngine() {
        return new Engine(20, TaskType.IO_INTENSIVE, 20);
    }

    @Bean(name = "producer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ProducerImpl getProducer(Engine engine, Logger log) {
        return new ProducerImpl(engine, log);
    }

    @Bean(name = "ioConsumer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer getConsumerIO(Engine engine, Logger log) {
        return new Consumer(engine, TaskType.IO_INTENSIVE, log);
    }

    @Bean(name = "cpuConsumer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer getConsumerCPU(Engine engine, Logger log) {
        return new Consumer(engine, TaskType.CPU_INTENSIVE, log);
    }
}
