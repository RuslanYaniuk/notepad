package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class LoginAlreadyTakenException extends ExceptionWithParams {

    private static final long serialVersionUID = 6465519007604145976L;

    public LoginAlreadyTakenException(String... args) {
        super("user.registration.error.alreadyTakenLogin");
        super.args = args;
    }
}
