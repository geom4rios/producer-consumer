package com.geom4rios.javaproducerconsumer.controller;

import com.geom4rios.javaproducerconsumer.Engine;
import com.geom4rios.javaproducerconsumer.controller.rest.RestEngine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private final Engine engine;

    public MonitorController(Engine engine) {
        this.engine = engine;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RestEngine getRestEngine() {
        return new RestEngine(engine);
    }
}
