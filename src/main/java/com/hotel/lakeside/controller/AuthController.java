package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUserService service;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody  User user){
        try {
            service.registerUser(user);
            return ResponseEntity.ok("Registration success");
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
