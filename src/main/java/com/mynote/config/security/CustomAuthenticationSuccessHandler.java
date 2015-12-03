package com.mynote.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mynote.config.web.ExtendedMessageSource;
import com.mynote.dto.LoginSuccessMessageDTO;
import com.mynote.dto.MessageDTO;
import com.mynote.dto.user.UserInfoDTO;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import com.mynote.utils.JsonResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String SESSION_ATTRIBUTE_USER_ID = "user_id";

    @Autowired
    private JsonResponseBuilder jsonResponseBuilder;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ExtendedMessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Locale locale = LocaleContextHolder.getLocale();
        MessageDTO messageDTO = messageSource.getMessageDTO("user.login.success", locale);
        LoginSuccessMessageDTO successDTO = new LoginSuccessMessageDTO();
        User user;

        try {
            user = userService.findUserById((Long) authentication.getDetails());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        successDTO.setMessage(messageDTO.getMessage());
        successDTO.setMessageCode(messageDTO.getMessageCode());
        successDTO.setUserDTO(new UserInfoDTO(user));

        session.setAttribute(SESSION_ATTRIBUTE_USER_ID, authentication.getDetails());

        jsonResponseBuilder.sendJson(response, SC_OK, jacksonObjectMapper.writeValueAsString(successDTO));
    }
}
