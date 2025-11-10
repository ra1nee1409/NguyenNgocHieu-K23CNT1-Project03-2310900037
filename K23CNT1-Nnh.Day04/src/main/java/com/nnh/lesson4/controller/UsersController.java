package com.nnh.lesson4.controller;
import com.nnh.lesson4.dto.UsersDTO;
import com.nnh.lesson4.entity.Users;
import com.nnh.lesson4.service.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class UsersController {
    @Autowired
    UsersService usersService;

    @GetMapping("/user-list")
    public List<Users> getAllUsers() {
        return usersService.findAll();
    }

    @PostMapping("/user-add")
    public ResponseEntity<String> addUser(@Valid @RequestBody UsersDTO user) {
        usersService.create(user);
        return ResponseEntity.badRequest().body("Users created successfully");
    }
}

