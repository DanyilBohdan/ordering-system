package com.restaurant.orderingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictIllegalStateException extends IllegalStateException{

    public ConflictIllegalStateException(String message) {
        super(message);
    }
}

