package com.mynote.test.unit.conf;

import com.mynote.config.session.HttpSessionContext;
import com.mynote.models.User;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
@Configuration
public class TestHttpSessionContext extends HttpSessionContext {

    private User user;

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
