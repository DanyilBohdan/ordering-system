package com.restaurant.orderingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoProductException extends RuntimeException{

    public NoProductException(String message) {
        super(message);
    }
}
