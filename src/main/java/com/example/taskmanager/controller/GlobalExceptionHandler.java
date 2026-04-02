package com.example.taskmanager.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleIllegalArgument(Exception e, Model model) {
        model.addAttribute("error", "서버 오류가 발생했습니다.");
        return "error";
    }
}
