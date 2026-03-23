package com.example.taskmanager.dto;

public class UserRegisterDTO {
    private String username;
    private String password;
    private String passwordConfirm; // 회원가입할 때 비번 두 번 입력하잖슴. 그거임 두 번째 입력하는 비번

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
