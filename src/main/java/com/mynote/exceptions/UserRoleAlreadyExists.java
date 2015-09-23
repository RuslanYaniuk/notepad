package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleAlreadyExists extends ExceptionWithParams {

    private static final long serialVersionUID = 3707543297392423309L;

    public UserRoleAlreadyExists(String... args) {
        super("user.role.exists");
        super.args = args;
    }
}
