package com.bazzi.app.application.exception;

public class InvalidUsernameException extends CustomException{
    public InvalidUsernameException(String message){
        super(message, 400);
    }
}
