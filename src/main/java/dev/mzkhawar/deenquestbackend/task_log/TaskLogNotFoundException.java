package dev.mzkhawar.deenquestbackend.task_log;

public class TaskLogNotFoundException extends RuntimeException {
    public TaskLogNotFoundException(String message) {
        super(message);
    }
}
