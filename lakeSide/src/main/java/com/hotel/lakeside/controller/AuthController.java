package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
public class AuthController {
    private final IUserService service;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user){
        try {
            service.registerUser(user);
            return ResponseEntity.ok("Registration success");
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
