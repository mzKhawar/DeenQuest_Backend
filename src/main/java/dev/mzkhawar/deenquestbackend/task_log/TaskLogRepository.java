package dev.mzkhawar.deenquestbackend.task_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

    List<TaskLog> findByUser_Id(Long userId);
    List<TaskLog> findByTask_Id(Long taskId);
    List<TaskLog> findByUser_IdAndTask_Id(Long userId, Long taskId);
    List<TaskLog> findByUser_IdAndCompletedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<TaskLog> findByUser_IdAndTask_IdAndCompletedAtBetween(Long userId, Long taskId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM TaskLog t WHERE t.user.id = :userId AND t.task.id = :taskId AND t.completedAt BETWEEN :fromDate AND :toDate ORDER BY t.completedAt ASC ")
    List<TaskLog> findByDateRangeForUserIdAndTaskId(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, @Param("userId") Long userId, @Param("taskId") Long taskId);
}