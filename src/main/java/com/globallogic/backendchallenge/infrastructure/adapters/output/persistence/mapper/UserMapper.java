package com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.mapper;

import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.dto.CreateUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.GetUserResponseDto;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PhoneMapper.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "userUuid", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "phones", source = "phones")
    UserEntity toEntity(CreateUserRequestDto request);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "id", source = "userUuid")
    CreateUserResponseDto toCreateResponseDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "password", ignore = true)
    GetUserResponseDto toGetResponseDto(UserEntity entity);
}
