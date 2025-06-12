package dev.mzkhawar.deenquestbackend.config;

import dev.mzkhawar.deenquestbackend.task.TaskNotFoundException;
import dev.mzkhawar.deenquestbackend.task_log.TaskLogNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TaskLogNotFoundException.class)
    public ResponseEntity<String> handleTaskLogNotFoundException(TaskLogNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
