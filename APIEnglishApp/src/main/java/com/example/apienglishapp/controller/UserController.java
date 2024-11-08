package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.ApiResponse;
import com.example.apienglishapp.dto.request.UserCreationRequest;
import com.example.apienglishapp.dto.request.UserUpdateRequest;
import com.example.apienglishapp.dto.response.UserResponse;
import com.example.apienglishapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostAuthorize("#id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @GetMapping (value = "/users/{id}")
    public ApiResponse<UserResponse> getUserById (@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(id))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping (value = "/users")
    public ApiResponse<List<UserResponse>> getAllUsers () {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @PostMapping (value = "/users")
    public ApiResponse<UserResponse> createUser (@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostAuthorize("#id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @PutMapping (value = "/users/{id}")
    public UserResponse updateUser (@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return userService.updateRequest(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping (value = "/users/{id}")
    public void deleteUser (@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping (value = "/myInfo")
    public ApiResponse<UserResponse> getMyInfo () {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
