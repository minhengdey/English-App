package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.AuthenticationRequest;
import com.example.apienglishapp.dto.request.IntrospectRequest;
import com.example.apienglishapp.dto.response.AuthenticationResponse;
import com.example.apienglishapp.dto.response.IntrospectResponse;
import com.example.apienglishapp.entity.UserEntity;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.exception.ErrorCode;
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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    public static final String SIGNER_KEY =
            "YD8HzTlo8PGQ4jbvy8JzRWDEPHj3ESBodMOy2VN18m34naEMGKl3PvThviChOLfY";

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiration.after(new Date()))
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

    private String generateToken(UserEntity userEntity) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userEntity.getUsername())
                .issuer("Minh Anh")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
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
