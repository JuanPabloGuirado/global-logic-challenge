package com.globallogic.backendchallenge.domain.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class UnableToCreateUserException extends RuntimeException {

    public UnableToCreateUserException(String s) {

    }
}
