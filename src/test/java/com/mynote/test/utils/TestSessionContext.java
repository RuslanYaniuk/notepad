package com.mynote.test.utils;

import com.mynote.models.User;
import com.mynote.utils.CustomSessionContext;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class TestSessionContext extends CustomSessionContext {

    private User user;
    private boolean mocked = false;
    private CustomSessionContext httpSessionContext;

    public TestSessionContext(CustomSessionContext httpSessionContext) {
        this.httpSessionContext = httpSessionContext;
    }

    @Override
    public UserDetails getUser() {
        if (mocked) {
            return user;
        }
        return httpSessionContext.getUser();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMocked(boolean mocked) {
        this.mocked = mocked;
    }
}
