package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.ApiResponse;
import com.example.apienglishapp.dto.request.ForgotPasswordRequest;
import com.example.apienglishapp.dto.request.UserCreationRequest;
import com.example.apienglishapp.dto.request.UserUpdateRequest;
import com.example.apienglishapp.dto.response.UserResponse;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
import com.example.apienglishapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public ApiResponse<UserResponse> createUser (@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping (value = "/users")
    public UserResponse updateUser (@Valid @RequestBody UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return userService.updateRequest(Long.parseLong(jwt.getSubject()), request);
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
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

    @PutMapping (value = "/users/forgot_password")
    public UserResponse forgotPassword (@Valid @RequestBody ForgotPasswordRequest request) {
        return userService.forgotPassword(request);
    }
}
