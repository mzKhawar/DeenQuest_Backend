package dev.mzkhawar.deenquestbackend.task_log;

import dev.mzkhawar.deenquestbackend.task.Task;
import dev.mzkhawar.deenquestbackend.task.TaskNotFoundException;
import dev.mzkhawar.deenquestbackend.task.TaskRepository;
import dev.mzkhawar.deenquestbackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskRepository taskRepository;
    private final TaskLogMapper taskLogMapper;

    public TaskLogResponse createTaskLog(TaskLogRequest taskLogRequest, User user) {
        Task task = taskRepository.findById(taskLogRequest.getTaskId()).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        TaskLog savedTask = taskLogRepository.save(TaskLog.builder()
                        .task(task)
                        .user(user)
                        .completedAt(taskLogRequest.getCompletedAt())
                        .build());
        return taskLogMapper.toResponse(savedTask);
    }

    public List<TaskLogResponse> findAll(User user) {
        return taskLogRepository.findByUser_Id(user.getId()).stream().map(taskLogMapper::toResponse).collect(Collectors.toList());
    }

    public TaskLogResponse findById(Long id) throws TaskLogNotFoundException {
        TaskLog taskLog = taskLogRepository.findById(id).orElseThrow(() -> new TaskLogNotFoundException("Task log not found."));
        return taskLogMapper.toResponse(taskLog);
    }

    public List<TaskLogResponse> findByDateRange(User user, LocalDateTime from, LocalDateTime to) {
        return taskLogRepository.findByUser_IdAndCompletedAtBetween(user.getId(), from, to).stream().map(taskLogMapper::toResponse).collect(Collectors.toList());
    }

    public void update(Long id, TaskLogRequest taskLogRequest) {
        TaskLog taskToUpdate = taskLogRepository.findById(id).orElseThrow(() -> new TaskLogNotFoundException("Task log not found."));
        taskToUpdate.setCompletedAt(taskLogRequest.getCompletedAt());
        taskLogRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        taskLogRepository.deleteById(id);
    }

    public List<TaskLogResponse> findByTaskAndDateRange(User user, Long taskId, LocalDateTime from, LocalDateTime to) {
        return taskLogRepository.findByDateRangeForUserIdAndTaskId(from, to, user.getId(), taskId).stream().map(taskLogMapper::toResponse).collect(Collectors.toList());
    }
}
