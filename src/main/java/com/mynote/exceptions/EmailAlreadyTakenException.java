package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class EmailAlreadyTakenException extends ExceptionWithParams {

    private static final long serialVersionUID = 6188029173371142043L;

    public EmailAlreadyTakenException(String... args) {
        super("user.registration.error.alreadyTakenEmail");
        super.args = args;
    }
}
