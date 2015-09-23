package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserNotFoundException extends ExceptionWithParams {

    private static final long serialVersionUID = 1190975595385084572L;

    public UserNotFoundException(String... args) {
        super("user.lookup.notFound");
        super.args = args;
    }
}
