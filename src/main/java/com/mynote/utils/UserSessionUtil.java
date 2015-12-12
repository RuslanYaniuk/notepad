package com.mynote.utils;

import com.mynote.models.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.mynote.config.security.CustomAuthenticationSuccessHandler.SESSION_ATTRIBUTE_USER;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserSessionUtil {

    public static User getCurrentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return (User) attr.getRequest().getSession(false).getAttribute(SESSION_ATTRIBUTE_USER);
    }
}
