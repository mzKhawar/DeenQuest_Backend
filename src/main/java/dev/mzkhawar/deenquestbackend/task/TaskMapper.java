package dev.mzkhawar.deenquestbackend.task;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(@NonNull Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .build();
    }

    public Task toEntity(@NonNull TaskRequest taskRequest) {
        return Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .build();
    }
}
