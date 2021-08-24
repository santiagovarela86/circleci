package com.tacs.grupo1.covid.api.exceptions;

public class RestRepositoryException extends RuntimeException {
    public RestRepositoryException() {
        super("Repository error.");
    }

    public RestRepositoryException(String message) {
        super(message);
    }
}
