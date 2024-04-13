package com.aws.cognito.serviceImpl;

import com.aws.cognito.bean.UserRegistrationRequest;
import com.aws.cognito.entity.User;
import com.aws.cognito.repository.UserRepository;
import com.aws.cognito.service.UserService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CognitoService cognitoService;

    @Override
    public JsonObject registerUser(UserRegistrationRequest userRegistrationRequest) {
        String username = userRegistrationRequest.getUsername();
        String password = userRegistrationRequest.getPassword();
        String email = userRegistrationRequest.getEmail();
        String number = userRegistrationRequest.getNumber();
        return cognitoService.registerUser(username, email, password, number);
    }

    @Override
    public JsonObject loginUser(String username, String password) {
        return cognitoService.loginUser(username, password);
    }
}
