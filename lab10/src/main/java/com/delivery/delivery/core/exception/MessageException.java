package com.delivery.delivery.core.exception;

public class MessageException extends RuntimeException {
    protected final int code;

    public MessageException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}