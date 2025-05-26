package dev.mzkhawar.deenquestbackend.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public void update(Long id, Task task) {
        Task taskToUpdate = findById(id);
        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());
        taskRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
