package com.globallogic.backendchallenge.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateUserRequestDto {

    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;
}
