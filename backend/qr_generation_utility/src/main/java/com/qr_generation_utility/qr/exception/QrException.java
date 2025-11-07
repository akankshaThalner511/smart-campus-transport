package com.qr_generation_utility.qr.exception;

import lombok.Getter;

@Getter
public class QrException extends RuntimeException {
    private final int errorCode;

    public QrException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
