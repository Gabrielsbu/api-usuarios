package com.dextra.makemagic.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AllException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public AllException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public AllException(String message) {
        super(message);
    }
}
