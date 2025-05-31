package dev.mzkhawar.deenquestbackend.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponse createTask(TaskRequest taskRequest) {
        Task savedTask = taskRepository.save(taskMapper.toEntity(taskRequest));
        return taskMapper.toResponse(savedTask);
    }

    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

    public TaskResponse findById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponse(task);
    }

    public void update(Long id, TaskRequest taskRequest) throws TaskNotFoundException {
        Task taskToUpdate = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskToUpdate.setName(taskRequest.getName());
        taskToUpdate.setDescription(taskRequest.getDescription());
        taskRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
