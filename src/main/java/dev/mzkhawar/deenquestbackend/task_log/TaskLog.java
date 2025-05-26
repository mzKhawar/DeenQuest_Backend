package dev.mzkhawar.deenquestbackend.task_log;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.mzkhawar.deenquestbackend.task.Task;
import dev.mzkhawar.deenquestbackend.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskLog {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "User id is required.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-taskLogs")
    private User user;

    @NotNull(message = "Completed at LocalDateTime required.")
    @Column(nullable = false)
    private LocalDateTime completedAt;

    @NotNull(message = "Task id is required.")
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
