package com.mynote.config.security;

import com.mynote.utils.JsonResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private JsonResponseBuilder jsonResponseBuilder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception != null) {
            jsonResponseBuilder.sendMessageDTO(response, SC_UNAUTHORIZED, exception.getMessage());
            return;
        }
        jsonResponseBuilder.sendMessageDTO(response, SC_UNAUTHORIZED, "Unauthorized.");
    }
}
