package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.UserDto;
import com.university.cyberwalk.model.User;
import com.university.cyberwalk.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                "USER");

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
