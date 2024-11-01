package com.example.apienglishapp.mapper;

import com.example.apienglishapp.dto.request.UserCreationRequest;
import com.example.apienglishapp.dto.request.UserUpdateRequest;
import com.example.apienglishapp.dto.response.UserResponse;
import com.example.apienglishapp.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserCreationRequest request);
    UserResponse toUserResponse(UserEntity entity);
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest request);
}
