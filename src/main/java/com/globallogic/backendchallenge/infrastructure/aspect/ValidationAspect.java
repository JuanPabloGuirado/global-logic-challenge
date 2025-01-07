package com.globallogic.backendchallenge.infrastructure.aspect;

import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.exception.InvalidInputDataException;
import com.globallogic.backendchallenge.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
public class ValidationAspect {

    @Before("@annotation(com.globallogic.backendchallenge.domain.validation.ValidateInputParams) && args(request, ..)")
    public void validateEmailAndPassword(CreateUserRequestDto request) {
        log.info(String.format("Validating input data - request body: %s", request));
        if (!Pattern.compile(Constants.EMAIL_FORMAT_VALIDATION_REGEX).matcher(request.getEmail()).matches()) {
            log.error(String.format("Invalid format for email: %s", request.getEmail()));
            throw new InvalidInputDataException("Invalid email format");
        }

        if (!Pattern.compile(Constants.PASSWORD_FORMAT_VALIDATION_REGEX).matcher(request.getPassword()).matches()) {
            log.error(String.format("Invalid format for password: %s", request.getPassword()));
            throw new InvalidInputDataException("Invalid password format");
        }
    }
}
