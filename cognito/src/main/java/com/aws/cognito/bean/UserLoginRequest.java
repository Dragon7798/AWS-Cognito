package com.aws.cognito.bean;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

public class UserLoginRequest {
    @NotBlank(message = "Username Can't be empty")
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
