package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.AuthenticationRequest;
import com.example.apienglishapp.dto.request.IntrospectRequest;
import com.example.apienglishapp.dto.request.LogoutRequest;
import com.example.apienglishapp.dto.request.RefreshRequest;
import com.example.apienglishapp.dto.response.AuthenticationResponse;
import com.example.apienglishapp.dto.response.IntrospectResponse;
import com.example.apienglishapp.dto.response.RefreshResponse;
import com.example.apienglishapp.entity.InvalidatedTokenEntity;
import com.example.apienglishapp.entity.UserEntity;
import com.example.apienglishapp.enums.Role;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
import com.example.apienglishapp.repository.InvalidatedTokenRepository;
import com.example.apienglishapp.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    public static final String SIGNER_KEY =
            "YD8HzTlo8PGQ4jbvy8JzRWDEPHj3ESBodMOy2VN18m34naEMGKl3PvThviChOLfY";

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        boolean isValid = true;
        try {
            SignedJWT signedJWT = verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(logoutRequest.getToken());
        String id = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedTokenEntity.builder()
                        .id(id)
                        .expiryTime(expiryTime)
                .build());
    }

    public RefreshResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken());
        String id = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedTokenEntity.builder()
                .id(id)
                .expiryTime(expiryTime)
                .build());
        UserEntity user = userRepository.findById(Long.valueOf(signedJWT
                .getJWTClaimsSet().getSubject()))
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String token = generateToken(user);
        return RefreshResponse.builder()
                .token(token)
                .success(true)
                .build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean success = passwordEncoder.matches(request.getPassword(), userEntity.getPassword());
        if (!success) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(userEntity);
        return AuthenticationResponse.builder()
                .token(token)
                .success(true)
                .build();
    }

    public AuthenticationResponse createAndLoginGoogle (OAuth2User user) {
        String email = user.getAttribute("email");
        try {
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            String token = generateToken(userEntity);
            return AuthenticationResponse.builder()
                    .success(true)
                    .token(token)
                    .build();
        } catch (AppException e) {
            Set<String> roles = new HashSet<>();
            roles.add(Role.USER.name());
            UserEntity userEntity = userRepository.save(UserEntity
                    .builder()
                    .email(email)
                    .username(user.getAttribute("name"))
                    .day(1)
                    .month(1)
                    .year(1)
                    .password("12345678")
                    .phone(user.getAttribute("phone"))
                    .roles(roles)
                    .build());
            String token = generateToken(userEntity);
            return AuthenticationResponse.builder()
                    .success(true)
                    .token(token)
                    .build();
        }
    }

    public String generateToken(UserEntity userEntity) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(userEntity.getId()))
                .issuer("Minh Anh")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1000, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(userEntity.getRoles()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope (Set<String> scopes) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String scope : scopes) {
            joiner.add(scope);
        }
        return joiner.toString();
    }
}
