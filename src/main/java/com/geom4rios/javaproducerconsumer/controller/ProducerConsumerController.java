package com.geom4rios.javaproducerconsumer.controller;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.Foreman;
import com.geom4rios.javaproducerconsumer.ProducerImpl;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine")
public class ProducerConsumerController {

    private final Engine engine;
    private final Foreman foreman;
    private final Logger log;

    public ProducerConsumerController(Engine engine, Foreman foreman, Logger log) {
        this.engine = engine;
        this.foreman = foreman;
        this.log = log;
    }

    @GetMapping
    public void startEngine() {
        // start engine
        ProducerImpl producer = new ProducerImpl(engine, log);
        foreman.addProducer(producer);
        foreman.start();
    }



}
