package com.geom4rios.javaproducerconsumer.controller;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.Foreman;
import com.geom4rios.javaproducerconsumer.examples.ProducerImpl;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import org.slf4j.Logger;
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
        Producer producer = new ProducerImpl();
        foreman.addProducer(producer);
        foreman.start();
    }



}
