package dev.mzkhawar.deenquestbackend.task_log;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mzkhawar.deenquestbackend.task.TaskResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskLogResponse {

    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime completedAt;
    @NotNull
    @JsonProperty("task")
    private TaskResponse taskResponse;
}
