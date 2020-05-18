package com.geom4rios.javaproducerconsumer.task;

/**
 * Enum that describes the type of {@link Task} to execute. <br>
 * A MEMORY_INTENSIVE task requires a lot of memory in order to be executed <br>
 * A CPU_INTENSIVE task requires a lot of cpu resources in order to run <br>
 * An IO_INTENSIVE task will wait for an io to complete, i.e rest request, db write etc, before it can complete it's work
 */
public enum TaskType {
    CPU_INTENSIVE,
    IO_INTENSIVE,
    MEMORY_INTENSIVE
}
