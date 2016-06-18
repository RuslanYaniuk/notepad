package com.mynote.exceptions;

import org.springframework.validation.Errors;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -8647644934916997569L;

    private Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
