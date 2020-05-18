package com.geom4rios.javaproducerconsumer.task;

/**
 * This is the interface to implement in order to execute a block of code asynchronously <br>
 * Write the code you want to be executed by overriding the execute() method. <br>
 * Return one of the available {@link TaskType task types} so that the application will know how to better handle your task
 */
public interface Task {
    TaskType getTaskType();

    void execute();
}
