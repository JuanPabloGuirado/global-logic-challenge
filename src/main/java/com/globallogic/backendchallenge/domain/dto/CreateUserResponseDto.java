package com.globallogic.backendchallenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserResponseDto {

    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String token;
    private Boolean isActive;
}
