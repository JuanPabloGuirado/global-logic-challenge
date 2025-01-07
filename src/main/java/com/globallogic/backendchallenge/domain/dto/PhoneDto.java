package com.globallogic.backendchallenge.domain.dto;

import lombok.Data;

@Data
public class PhoneDto {

    private Long number;
    private Integer cityCode;
    private String countryCode;
}
