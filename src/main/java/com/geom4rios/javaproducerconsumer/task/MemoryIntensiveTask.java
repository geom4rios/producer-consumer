package com.geom4rios.javaproducerconsumer.task;

/**
 * In case of a MemoryIntensiveTask you need to provide the amount of memory that the task will probably use, measured in MB.
 */
public interface MemoryIntensiveTask extends Task {
    int memoryRequiredToRun();

    @Override
    public default TaskType getTaskType() {
        return TaskType.MEMORY_INTENSIVE;
    }
}
