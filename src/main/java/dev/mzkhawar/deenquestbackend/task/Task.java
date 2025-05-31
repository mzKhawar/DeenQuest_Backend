package dev.mzkhawar.deenquestbackend.task;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.mzkhawar.deenquestbackend.task_log.TaskLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Task name is required.")
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonManagedReference("task-taskLogs")
    private List<TaskLog> taskLogs;
}
