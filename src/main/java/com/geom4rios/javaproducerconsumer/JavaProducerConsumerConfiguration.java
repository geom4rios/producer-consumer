package com.geom4rios.javaproducerconsumer;

import com.geom4rios.javaproducerconsumer.consumer.Consumer;
import com.geom4rios.javaproducerconsumer.consumer.ConsumerDistributor;
import com.geom4rios.javaproducerconsumer.producer.ProducerRunner;
import com.geom4rios.javaproducerconsumer.task.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class JavaProducerConsumerConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint injectionPoint){
        if (injectionPoint.getMethodParameter() != null) {
            return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass().getName());
        } else {
            return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        }
    }

    @Bean(name = "producerExecutor")
    public ExecutorService getProducerExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean(name = "distributorExecutor")
    public ExecutorService getDistributorExecutor() {
        return Executors.newSingleThreadExecutor();
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
    public ExecutorService getIoIntensiveExecutor() {
        return Executors.newFixedThreadPool(16);
    }

    @Bean(name = "memoryIntensiveExecutor")
    public ExecutorService getMemoryIntensiveExecutor() {
        return Executors.newFixedThreadPool(16);
    }

    @Bean
    public Engine getEngine() {
        return new Engine();
    }

    @Bean
    public ConsumerDistributor consumerDistributor(Engine engine, Logger log) {
        return new ConsumerDistributor(engine, log);
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

    @Bean(name = "memoryConsumer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer getConsumerMemory(Engine engine, Logger log) {
        return new Consumer(engine, TaskType.MEMORY_INTENSIVE, log);
    }
}
