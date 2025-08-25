package com.hcmute.clothingstore.exception;

public class APIException extends RuntimeException{
    public static final long serialVersionUID = 1;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
