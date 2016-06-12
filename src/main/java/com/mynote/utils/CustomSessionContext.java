package com.mynote.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class CustomSessionContext {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserDetails getUser() {
        return (UserDetails) getAuthentication().getPrincipal();
    }
}
