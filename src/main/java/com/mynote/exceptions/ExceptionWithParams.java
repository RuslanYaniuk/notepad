package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public abstract class ExceptionWithParams extends Exception {

    private static final long serialVersionUID = 7053598894135661102L;

    protected String[] args;

    public ExceptionWithParams(String message) {
        super(message);
    }

    public String[] getArgs() {
        return args;
    }
}
