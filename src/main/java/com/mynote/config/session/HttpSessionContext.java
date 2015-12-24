package com.mynote.config.session;

import com.mynote.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static com.mynote.config.security.CustomAuthenticationSuccessHandler.SESSION_ATTRIBUTE_USER;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
@Component
public class HttpSessionContext {

    public User getUser() {
        ServletRequestAttributes attr;
        HttpSession session;

        try {
            attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {
            User user = new User();

            user.setId(-1L);
            return user;
        }

        session = attr.getRequest().getSession(false);
        if (session == null) {
            throw new IllegalStateException("Request doesn't have session");
        }

        return (User) session.getAttribute(SESSION_ATTRIBUTE_USER);
    }
}
