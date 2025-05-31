package dev.mzkhawar.deenquestbackend.task_log;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLogRequest {

    @NotNull(message = "Task id is required.")
    private Long taskId;

    @NotNull(message = "Completed At LocalDateTime required.")
    private LocalDateTime completedAt;
}
