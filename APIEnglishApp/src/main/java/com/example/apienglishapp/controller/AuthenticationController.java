package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.ApiResponse;
import com.example.apienglishapp.dto.request.AuthenticationRequest;
import com.example.apienglishapp.dto.request.IntrospectRequest;
import com.example.apienglishapp.dto.response.AuthenticationResponse;
import com.example.apienglishapp.dto.response.IntrospectResponse;
import com.example.apienglishapp.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping (value = "/auth/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
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
}
