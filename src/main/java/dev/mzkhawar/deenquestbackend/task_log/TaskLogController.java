package dev.mzkhawar.deenquestbackend.task_log;

import dev.mzkhawar.deenquestbackend.task.Task;
import dev.mzkhawar.deenquestbackend.task.TaskService;
import dev.mzkhawar.deenquestbackend.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task-logs")
@RequiredArgsConstructor
public class TaskLogController {

    private final TaskLogService taskLogService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTaskLog(@RequestBody @Valid TaskLogRequest taskLogRequest, @AuthenticationPrincipal User user, UriComponentsBuilder ucb) {
        Task task = taskService.findById(taskLogRequest.getTaskId());
        TaskLog taskLogToSave = TaskLog.builder()
                .task(task)
                .user(user)
                .completedAt(taskLogRequest.getCompletedAt())
                .build();
        TaskLog savedTask = taskLogService.save(taskLogToSave);
        URI location = ucb.path("/api/v1/task-logs/{id}").build(savedTask.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<TaskLog>> getTaskLogs(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskLogService.findAll(user));
    }

    @GetMapping(value = "/date-range", params = {"from", "to"})
    public ResponseEntity<List<TaskLog>> getTaskLogsBetweenDates(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(taskLogService.findBetweenDates(user, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskLog> getTaskLogById(@PathVariable Long id) {
        TaskLog taskLog = taskLogService.findById(id);
        return ResponseEntity.ok(taskLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTaskLog(@PathVariable Long id, @RequestBody TaskLog taskLog) {
        taskLogService.update(id, taskLog);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskLog(@PathVariable Long id) {
        taskLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
