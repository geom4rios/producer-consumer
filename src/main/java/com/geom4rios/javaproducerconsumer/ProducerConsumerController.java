package com.geom4rios.javaproducerconsumer;

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
        foreman.start();
    }



}
