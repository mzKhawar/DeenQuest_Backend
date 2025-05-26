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

    @NotNull
    private Long taskId;
    @NotNull
    private LocalDateTime completedAt;
}
