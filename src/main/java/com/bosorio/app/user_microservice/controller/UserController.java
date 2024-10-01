package com.bosorio.app.user_microservice.controller;

import com.bosorio.app.user_microservice.DTO.CreateUserDTO;
import com.bosorio.app.user_microservice.DTO.LoginUserDTO;
import com.bosorio.app.user_microservice.DTO.UpdatePasswordDTO;
import com.bosorio.app.user_microservice.Exception.BadRequestException;
import com.bosorio.app.user_microservice.Exception.NotFoundException;
import com.bosorio.app.user_microservice.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            userService.create(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User created successfully");
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserDTO userDTO) {
        try {
            String token = userService.login(userDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(token);
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.getUsers());
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                   .body(userService.getUserById(id));
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody CreateUserDTO userDTO, @PathVariable Long id) {
        try {
            userService.update(userDTO, id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User updated successfully");
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @PatchMapping("/{id}/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO userDTO, @PathVariable Long id) {
        try {
            userService.updatePassword(userDTO, id);
            return ResponseEntity.status(HttpStatus.OK)
                   .body("Password updated successfully");
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User deleted successfully");
        } catch (Exception e) {
            return handleExceptions(e);
        }
    }

    private ResponseEntity<?> handleExceptions(Exception e) {
        Map<String, String> message = new HashMap<>();
        message.put("error", e.getMessage());
        if (e instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (e instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

}
