package com.globallogic.backendchallenge.domain.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class RecordAlreadyExistsException extends RuntimeException {

    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
