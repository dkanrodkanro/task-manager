package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskCreateDTO;
import com.example.taskmanager.dto.TaskUpdateDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void makeTask(TaskCreateDTO dto, User user) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        taskRepository.save(task);
    }

    public Task findOne(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("할 일을 찾을 수 없습니다."));

        return task;
    }

    public void updateTask(TaskUpdateDTO dto, User user, Long taskId) {
        Task task = findOne(taskId);

        if (!task.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        taskRepository.save(task);
    }

    public void toggleComplete(User user, Long taskId) {
        Task task = findOne(taskId);

        if (!task.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    public void deleteTask(User user, Long taskId) {
        Task task = findOne(taskId);

        if (!task.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        taskRepository.delete(task);
    }
}
