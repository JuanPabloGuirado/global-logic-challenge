package com.globallogic.backendchallenge.domain.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class UnableToRetrieveUserException extends RuntimeException {

    public UnableToRetrieveUserException(String message) {
        super(message);
    }
}
