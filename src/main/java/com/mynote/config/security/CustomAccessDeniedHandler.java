package com.mynote.config.security;

import com.mynote.utils.JsonResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private JsonResponseBuilder jsonResponseBuilder;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException != null) {
            jsonResponseBuilder.sendMessageDTO(response, SC_UNAUTHORIZED, accessDeniedException.getMessage());
            return;
        }
        jsonResponseBuilder.sendMessageDTO(response, SC_UNAUTHORIZED, "Unauthorized.");
    }
}
