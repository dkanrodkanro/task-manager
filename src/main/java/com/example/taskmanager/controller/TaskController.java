package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskCreateDTO;
import com.example.taskmanager.dto.TaskUpdateDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("tasks")
    public String getTaskList(Principal principal, Model model, @RequestParam(defaultValue = "date") String sort){
        String username = principal.getName();
        User user = userService.findUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Task> tasks;
        if (sort.equals("titleAsc")) {
            tasks = taskService.findByUserIdOrderByTitleAsc(user.getId());
        } else if (sort.equals("titleDesc")) {
            tasks = taskService.findByUserIdOrderByTitleDesc(user.getId());
        } else {
            tasks = taskService.findByUserIdOrderByCreatedAtDesc(user.getId());
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", username);
        model.addAttribute("sort", sort);
        return "tasks";
    }

    @GetMapping("tasks/new")
    public String getCreateTask(Model model) {
        model.addAttribute("taskCreateDTO", new TaskCreateDTO());
        return "new";
    }

    @PostMapping("/tasks/new")
    public String postCreateTask(@Valid @ModelAttribute TaskCreateDTO dto, BindingResult result, Principal principal) {
        if(result.hasErrors()) {
            return "new";
        }

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
        model.addAttribute("taskUpdateDTO", new TaskUpdateDTO());
        model.addAttribute("task", task);
        return "edit";
    }

    @PostMapping("tasks/{id}/edit")
    public String postEditTask(@PathVariable Long id, @Valid @ModelAttribute TaskUpdateDTO dto, BindingResult result, Principal principal, Model model) {
        if(result.hasErrors()) {
            Task task = taskService.findOne(id);
            model.addAttribute("task", task);
            return "edit";
        }

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
