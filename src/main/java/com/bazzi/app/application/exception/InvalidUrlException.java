package com.bazzi.app.application.exception;

public class InvalidUrlException extends CustomException{
    public InvalidUrlException(String message){
        super(message, 400);
    }
}
