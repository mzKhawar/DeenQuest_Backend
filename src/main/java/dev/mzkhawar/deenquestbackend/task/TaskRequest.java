package dev.mzkhawar.deenquestbackend.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    @NotBlank(message = "Task name is required.")
    private String name;
    private String description;
}
