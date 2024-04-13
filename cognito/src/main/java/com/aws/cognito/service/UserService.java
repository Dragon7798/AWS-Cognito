package com.aws.cognito.service;

import com.aws.cognito.bean.UserRegistrationRequest;
import com.aws.cognito.entity.User;
import com.google.gson.JsonObject;


public interface UserService {
    JsonObject registerUser(UserRegistrationRequest userRegistrationRequest);
    JsonObject loginUser(String username, String password);
}
