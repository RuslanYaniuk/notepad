package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleNotFoundException extends ExceptionWithParams {

    private static final long serialVersionUID = -7461595204300510791L;

    public UserRoleNotFoundException(String... args) {
        super("user.role.notFound");
        super.args = args;
    }
}
