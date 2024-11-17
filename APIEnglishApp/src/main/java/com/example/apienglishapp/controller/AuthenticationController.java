package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.*;
import com.example.apienglishapp.dto.response.AuthenticationResponse;
import com.example.apienglishapp.dto.response.IntrospectResponse;
import com.example.apienglishapp.dto.response.RefreshResponse;
import com.example.apienglishapp.dto.response.UserResponse;
import com.example.apienglishapp.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping (value = "/auth/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticated = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticated)
                .build();
    }

    @PostMapping (value = "/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping (value = "/auth/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping (value = "/refresh")
    public ApiResponse<RefreshResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<RefreshResponse>builder()
                .result(authenticationService.refresh(request))
                .build();
    }

    @GetMapping(value = "/auth/google-login")
    public ApiResponse<AuthenticationResponse> googleLogin() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        OAuth2User user = authentication.getPrincipal();
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.createAndLoginGoogle(user))
                .build();
    }
}
