package com.geom4rios.javaproducerconsumer.controller;

import com.geom4rios.javaproducerconsumer.Foreman;
import com.geom4rios.javaproducerconsumer.examples.CpuProducer;
import com.geom4rios.javaproducerconsumer.examples.IOProducer;
import com.geom4rios.javaproducerconsumer.examples.MemoryProducer;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine")
public class ProducerConsumerController {

    private final Foreman foreman;

    public ProducerConsumerController(Foreman foreman) {
        this.foreman = foreman;
    }

    @GetMapping
    public void startEngine() {
        // start engine
        Producer producer = new IOProducer();
        Producer producer1 = new CpuProducer();
        Producer producer2 = new MemoryProducer();
        foreman.addProducer(producer);
        foreman.addProducer(producer1);
        foreman.addProducer(producer2);
        foreman.start();
    }



}
