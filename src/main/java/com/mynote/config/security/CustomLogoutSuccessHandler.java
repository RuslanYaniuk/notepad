package com.mynote.config.security;

import com.mynote.dto.MessageDTO;
import com.mynote.utils.CustomMessageSource;
import com.mynote.utils.JsonResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JsonResponseBuilder jsonResponseBuilder;

    @Autowired
    private CustomMessageSource messageSource;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Locale locale = LocaleContextHolder.getLocale();
        MessageDTO messageDTO = messageSource.getMessageDTO("user.logout.success", locale);

        jsonResponseBuilder.sendMessageDTO(response, SC_OK, messageDTO);
    }
}
