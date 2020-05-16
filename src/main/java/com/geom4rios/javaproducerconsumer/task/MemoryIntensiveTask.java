package com.geom4rios.javaproducerconsumer.task;

public interface MemoryIntensiveTask extends Task {
    int memoryRequiredToRun();
}
