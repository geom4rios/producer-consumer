package com.geom4rios.javaproducerconsumer;

public class Task {

    private final TaskType taskType = TaskType.IO_INTENSIVE;

    public void execute() {
        if (this.taskType == TaskType.CPU_INTENSIVE) {
            // do some cpu intensive work that requires some time to run and will consume significant cpu resources
            System.out.println("SOME CPU INTENSIVE TASK");
        } else if (this.taskType == TaskType.IO_INTENSIVE) {
            // do some IO intensive task, i.e write to file or db or fetch data from an external service
            System.out.println("SOME IO INTENSIVE TASK");
        } else {
            throw new UnsupportedOperationException("Task type not supported!");
        }
    }
}
