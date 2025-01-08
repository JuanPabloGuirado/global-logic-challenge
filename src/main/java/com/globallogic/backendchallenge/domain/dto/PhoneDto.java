package com.globallogic.backendchallenge.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDto {

    private Long number;
    private Integer cityCode;
    private String countryCode;
}
