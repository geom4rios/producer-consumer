package com.geom4rios.javaproducerconsumer;

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

    @Bean
    public ExecutorService getExecutor() {
        return Executors.newFixedThreadPool(20);
    }

    @Bean
    public Engine getEngine() {
        return new Engine(20, TaskType.IO_INTENSIVE, 20);
    }

    @Bean(name = "producer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Producer getProducer(Engine engine, Logger log) {
        return new Producer(engine, log);
    }

    @Bean(name = "consumer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer getConsumer(Engine engine, Logger log) {
        return new Consumer(engine, log);
    }
}
