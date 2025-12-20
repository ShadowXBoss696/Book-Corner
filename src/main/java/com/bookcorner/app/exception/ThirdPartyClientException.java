package com.bookcorner.app.exception;

public class ThirdPartyClientException extends Exception {

    public ThirdPartyClientException(String message) {
        super(message);
    }

    public ThirdPartyClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
