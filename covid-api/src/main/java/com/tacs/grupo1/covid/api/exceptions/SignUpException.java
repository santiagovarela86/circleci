package com.tacs.grupo1.covid.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SignUpException extends RuntimeException {
    public SignUpException(String message) {
        super(message);
    }
}
