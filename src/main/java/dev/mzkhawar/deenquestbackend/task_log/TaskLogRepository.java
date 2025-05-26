package dev.mzkhawar.deenquestbackend.task_log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

    List<TaskLog> findByUser_Id(Long userId);
    List<TaskLog> findByTask_Id(Long taskId);
    List<TaskLog> findByUser_IdAndCompletedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
