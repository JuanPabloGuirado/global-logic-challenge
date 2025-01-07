package com.globallogic.backendchallenge.infrastructure.adapters.input.controller;

import com.globallogic.backendchallenge.application.service.UserService;
import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.dto.CreateUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.GetUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<GetUserResponseDto> getUser(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(userService.getUserByToken(token));
    }

    @PostMapping(value = "/sign-up", produces = "application/json")
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
