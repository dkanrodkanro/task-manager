package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskCreateDTO;
import com.example.taskmanager.dto.TaskUpdateDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("tasks")
    public String getTaskList(Principal principal, Model model){
        String username = principal.getName();
        User user = userService.findUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        model.addAttribute("tasks", taskService.findByUserIdOrderByCreatedAtDesc(user.getId()));
        model.addAttribute("username", username);
        return "tasks";
    }

    @GetMapping("tasks/new")
    public String getCreateTask() {
        return "new";
    }

    @PostMapping("/tasks/new")
    public String postCreateTask(@ModelAttribute TaskCreateDTO dto, Principal principal) {
        String username = principal.getName();
        User user = userService.findUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        taskService.makeTask(dto, user);
        return "redirect:/tasks";
    }

    @GetMapping("tasks/{id}/edit")
    public String getEditTask(@PathVariable Long id, Principal principal, Model model) {
        String username = principal.getName();
        Task task = taskService.findOne(id);

        if (!task.getUser().getUsername().equals(username)) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "edit";
    }

    @PostMapping("tasks/{id}/edit")
    public String postEditTask(@PathVariable Long id, @ModelAttribute TaskUpdateDTO dto, Principal principal) {
        String username = principal.getName();
        User user = userService.findUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        taskService.updateTask(dto, user, id);
        return "redirect:/tasks";
    }

    @PostMapping("tasks/{id}/complete")
    public String postComplete(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.findUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        taskService.toggleComplete(user, id);
        return "redirect:/tasks";
    }

    @PostMapping("tasks/{id}/delete")
    public String postDelete(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        User user = userService.findUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        taskService.deleteTask(user, id);
        return "redirect:/tasks";
    }
}
