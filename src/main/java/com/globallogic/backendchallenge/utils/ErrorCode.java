package com.globallogic.backendchallenge.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_DATA(1001),
    USER_ALREADY_EXISTS(1002),
    USER_NOT_CREATED(1003),
    INTERNAL_ERROR(1004),
    UNAUTHORIZED(1005),
    INTERNAL_SECURITY_ERROR(1006);

    private final Integer code;
}
