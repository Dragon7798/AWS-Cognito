package com.aws.cognito.bean;

import jakarta.validation.constraints.NotBlank;

public class UserRegistrationRequest {

    @NotBlank(message = "Username Can't be empty")
    private String username;
    private String password;
    @NotBlank(message = "email Can't be empty")
    private String email;
    @NotBlank(message = "DOB Can't be empty")
    private String dob;
    private String number;

    // Getters and setters for the fields

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
