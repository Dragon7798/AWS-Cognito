package com.aws.cognito.exceptionHandler;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validation(MethodArgumentNotValidException exception) {
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", false);
        responseMap.put("statusCode", 400);
        responseMap.put("message", exception.getDetailMessageArguments()[1]);

        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<Object> user(UsernameExistsException exception) {
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", false);
        responseMap.put("statusCode", 400);
        responseMap.put("message", exception.getErrorMessage());

        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Object> authorized(NotAuthorizedException exception) {
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", false);
        responseMap.put("statusCode", 401);
        responseMap.put("message", exception.getErrorMessage());

        return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generic(Exception exception) {
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", false);
        responseMap.put("statusCode", 500);
        responseMap.put("message", "Internal Server Error");

        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
