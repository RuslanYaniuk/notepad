package com.mynote.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mynote.dto.LoginSuccessMessageDTO;
import com.mynote.dto.MessageDTO;
import com.mynote.dto.user.UserInfoDTO;
import com.mynote.models.User;
import com.mynote.utils.CustomMessageSource;
import com.mynote.utils.JsonResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JsonResponseBuilder jsonResponseBuilder;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private CustomMessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        MessageDTO messageDTO = messageSource.getMessageDTO("user.login.success", LocaleContextHolder.getLocale());
        LoginSuccessMessageDTO successDTO = new LoginSuccessMessageDTO();
        User userDetails = (User) authentication.getPrincipal();

        successDTO.setMessage(messageDTO.getMessage());
        successDTO.setMessageCode(messageDTO.getMessageCode());
        successDTO.setUserDTO(new UserInfoDTO(userDetails));
        jsonResponseBuilder.sendJson(response, SC_OK, jacksonObjectMapper.writeValueAsString(successDTO));
    }
}
