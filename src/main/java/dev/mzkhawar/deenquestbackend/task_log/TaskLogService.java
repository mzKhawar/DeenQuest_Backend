package dev.mzkhawar.deenquestbackend.task_log;

import dev.mzkhawar.deenquestbackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;

    public TaskLog save(TaskLog taskLog) {
        return taskLogRepository.save(taskLog);
    }

    public List<TaskLog> findAll(User user) {
        return taskLogRepository.findByUser_Id(user.getId());
    }

    public TaskLog findById(Long id) {
        return taskLogRepository.findById(id).orElseThrow(() -> new TaskLogNotFoundException("Task log not found."));
    }

    public List<TaskLog> findBetweenDates(User user, LocalDateTime from, LocalDateTime to) {
        return taskLogRepository.findByUser_IdAndCompletedAtBetween(user.getId(), from, to);
    }

    public void update(Long id, TaskLog taskLog) {
        TaskLog taskToUpdate = findById(id);
        taskToUpdate.setCompletedAt(taskLog.getCompletedAt());
        taskLogRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        taskLogRepository.deleteById(id);
    }
}
