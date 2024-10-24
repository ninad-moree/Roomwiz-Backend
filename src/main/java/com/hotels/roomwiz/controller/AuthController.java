package com.hotels.roomwiz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotels.roomwiz.exception.UserAlreadyExistsException;
import com.hotels.roomwiz.model.User;
import com.hotels.roomwiz.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user) {
        try {
            userService.registeredUser(user);
            return ResponseEntity.ok("user registered successfully");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
