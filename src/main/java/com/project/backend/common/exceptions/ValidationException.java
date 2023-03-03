package com.project.backend.common.exceptions;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationException extends RuntimeException{
    private Errors errors;
    private Object[] args;

    public ValidationException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message, Errors errors, Object[] args) {
        super(message);
        this.errors = errors;
        this.args = args;
    }

    public ValidationException(String message, Object[] args){
        super(message);
        this.args = args;
    }
}
