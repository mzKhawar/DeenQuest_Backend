package dev.mzkhawar.deenquestbackend.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String description;
}