package com.bazzi.app.application.exception;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException(String message) {
        super(message, 401);
    }
}
