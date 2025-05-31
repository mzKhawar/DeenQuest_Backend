package dev.mzkhawar.deenquestbackend.task_log;

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
    public ResponseEntity<Void> createTaskLog(
            @RequestBody @Valid TaskLogRequest taskLogRequest, @AuthenticationPrincipal User user, UriComponentsBuilder ucb) {
        TaskLogResponse taskLogResponse = taskLogService.createTaskLog(taskLogRequest, user);
        URI location = ucb.path("/api/v1/task-logs/{id}").build(taskLogResponse.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<TaskLogResponse>> getTaskLogs(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskLogService.findAll(user));
    }

    @GetMapping(value = "/date-range", params = {"from", "to"})
    public ResponseEntity<List<TaskLogResponse>> getTaskLogsByDateRange(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(taskLogService.findByDateRange(user, from, to));
    }

    @GetMapping(value = "/date-range/{task-id}", params = {"from", "to"})
    public ResponseEntity<List<TaskLogResponse>> getTaskLogsByTaskAndDateRange(
            @PathVariable("task-id") Long taskId,
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(taskLogService.findByTaskAndDateRange(user, taskId, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskLogResponse> getTaskLogById(@PathVariable Long id) {
        return ResponseEntity.ok(taskLogService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTaskLog(@PathVariable Long id, @RequestBody @Valid TaskLogRequest taskLogRequest) {
        taskLogService.update(id, taskLogRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskLog(@PathVariable Long id) {
        taskLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
