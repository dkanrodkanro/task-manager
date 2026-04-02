package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskCreateDTO {
    @NotBlank(message="제목을 입력해주세요")
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
