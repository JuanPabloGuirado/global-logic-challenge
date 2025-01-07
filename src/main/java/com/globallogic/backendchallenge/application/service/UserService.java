package com.globallogic.backendchallenge.application.service;

import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.dto.CreateUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.GetUserResponseDto;

public interface UserService {

    CreateUserResponseDto createUser(CreateUserRequestDto request);

    GetUserResponseDto getUserByToken(String token);
}
