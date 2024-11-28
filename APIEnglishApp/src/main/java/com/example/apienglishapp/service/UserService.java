package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.ForgotPasswordRequest;
import com.example.apienglishapp.dto.request.UserCreationRequest;
import com.example.apienglishapp.dto.request.UserUpdateRequest;
import com.example.apienglishapp.dto.response.UserResponse;
import com.example.apienglishapp.entity.UserEntity;
import com.example.apienglishapp.enums.Role;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
import com.example.apienglishapp.mapper.UserMapper;
import com.example.apienglishapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserResponse createUser (UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) ||
                userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity userEntity = userMapper.toUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        userEntity.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public UserResponse updateRequest (Long id, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userMapper.updateUser(userEntity, request);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public UserResponse forgotPassword (ForgotPasswordRequest forgotPassword) {
        UserEntity userEntity = userRepository.findByEmail(forgotPassword.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userEntity.setPassword(passwordEncoder.encode(forgotPassword.getNewPassword()));
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public UserResponse findById (Long id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public void deleteById (Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getMyInfo () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            Long id = Long.parseLong(jwt.getSubject());
            UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            return userMapper.toUserResponse(userEntity);
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
}
