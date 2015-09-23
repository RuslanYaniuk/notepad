package com.mynote.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class AccountIsDisabledException extends AuthenticationException {

    private static final long serialVersionUID = 6475754140032492939L;

    private String[] args;

    public AccountIsDisabledException(String... args) {
        super("user.login.error.accountIsDisabled");
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}
