package com.globallogic.backendchallenge.domain.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private InnerError error;

    @Data
    @Builder
    public static class InnerError {
        private LocalDateTime timestamp;
        private Integer code;
        private String detail;
    }
}
