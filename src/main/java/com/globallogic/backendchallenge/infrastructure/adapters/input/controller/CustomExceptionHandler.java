package com.globallogic.backendchallenge.infrastructure.adapters.input.controller;

import com.globallogic.backendchallenge.domain.dto.ErrorResponse;
import com.globallogic.backendchallenge.domain.exception.*;
import com.globallogic.backendchallenge.utils.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidInputDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.InnerError.builder()
                        .code(ErrorCode.INVALID_DATA.getCode())
                        .detail(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build());
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleExistingUserException(RecordAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.InnerError.builder()
                        .code(ErrorCode.USER_ALREADY_EXISTS.getCode())
                        .detail(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build());
    }

    @ExceptionHandler(UnableToCreateUserException.class)
    public ResponseEntity<ErrorResponse> handleUserNotCreatedException(UnableToCreateUserException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.InnerError.builder()
                        .code(ErrorCode.USER_NOT_CREATED.getCode())
                        .detail(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build());
    }

    @ExceptionHandler(UnableToRetrieveUserException.class)
    public ResponseEntity<ErrorResponse> handleUserNotRetrievedException(UnableToRetrieveUserException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.InnerError.builder()
                        .code(ErrorCode.INTERNAL_ERROR.getCode())
                        .detail(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build())
                .build());
    }

    @ExceptionHandler(EncryptionException.class)
    public ResponseEntity<ErrorResponse> handleEncryptionException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.InnerError.builder()
                                .code(ErrorCode.INTERNAL_SECURITY_ERROR.getCode())
                                .detail("Internal error")
                                .timestamp(LocalDateTime.now())
                                .build())
                        .build());
    }


}
