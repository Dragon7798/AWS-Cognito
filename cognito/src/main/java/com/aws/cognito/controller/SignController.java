package com.aws.cognito.controller;

import com.aws.cognito.bean.UserLoginRequest;
import com.aws.cognito.bean.UserRegistrationRequest;
import com.aws.cognito.serviceImpl.UserServiceImpl;
import com.aws.cognito.utility.TokenUtility;
import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/register", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        JsonObject response = userService.registerUser(userRegistrationRequest);
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

    }

    @GetMapping("/user")
    public String getUserDetails(@RequestBody String token) {
        TokenUtility.validateJwt(token);
        return "User: ";
    }

    @PostMapping(value = "/login", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        JsonObject response = userService.loginUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

    }
}
