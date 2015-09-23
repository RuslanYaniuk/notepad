package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class OperationNotPermitted extends ExceptionWithParams {

    private static final long serialVersionUID = -3579508739676895053L;

    public OperationNotPermitted(String... args) {
        super("action.notPermitted");
        super.args = args;
    }
}
