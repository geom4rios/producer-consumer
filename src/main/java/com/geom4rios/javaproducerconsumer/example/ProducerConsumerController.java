package com.geom4rios.javaproducerconsumer.example;

import com.geom4rios.javaproducerconsumer.Foreman;
import com.geom4rios.javaproducerconsumer.producer.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/example")
public class ProducerConsumerController {

    private final Foreman foreman;

    public ProducerConsumerController(Foreman foreman) {
        this.foreman = foreman;
    }

    @GetMapping
    public void example() {
        Producer producer = new IOProducer();
        Producer producer1 = new CpuProducer();
        Producer producer2 = new MemoryProducer();
        foreman.addProducer(producer);
        foreman.addProducer(producer1);
        foreman.addProducer(producer2);
    }
}
