package dev.mzkhawar.deenquestbackend.task_log;

import dev.mzkhawar.deenquestbackend.task.Task;
import dev.mzkhawar.deenquestbackend.task.TaskResponse;
import dev.mzkhawar.deenquestbackend.user.User;
import org.springframework.stereotype.Component;

@Component
public class TaskLogMapper {

    public TaskLogResponse toResponse(TaskLog taskLog) {
        return TaskLogResponse.builder()
                .id(taskLog.getId())
                .completedAt(taskLog.getCompletedAt())
                .taskResponse(
                        TaskResponse.builder()
                                .id(taskLog.getTask().getId())
                                .name(taskLog.getTask().getName())
                                .description(taskLog.getTask().getDescription())
                                .build()
                )
                .build();
    }

    public TaskLog toEntity(TaskLogRequest taskLogRequest, User user, Task task) {
        return TaskLog.builder()
                .user(user)
                .task(task)
                .completedAt(taskLogRequest.getCompletedAt())
                .build();
    }
}

