package com.mynote.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mynote.config.Constants;
import com.mynote.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import static com.mynote.config.Constants.APPLICATION_ENCODING;
import static com.mynote.config.Constants.APPLICATION_JSON_UTF8;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Component
public class JsonResponseBuilder {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private CustomMessageSource messageSource;

    public void sendMessageDTO(HttpServletResponse response, Integer statusCode, MessageDTO messageDTO) throws IOException {
        sendJson(response, statusCode, jacksonObjectMapper.writeValueAsString(messageDTO));
    }

    public void sendMessageDTO(HttpServletResponse response, Integer statusCode, String msg) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        MessageDTO jsonResponse;

        try {
            jsonResponse = messageSource.getMessageDTO(msg, locale);
        } catch (NoSuchMessageException e) {
            jsonResponse = new MessageDTO(Constants.SYSTEM_MESSAGE_CODE, msg);
        }
        sendJson(response, statusCode, jacksonObjectMapper.writeValueAsString(jsonResponse));
    }

    public void sendJson(HttpServletResponse response, Integer statusCode, String json) throws IOException {
        PrintWriter out;

        response.setContentType(APPLICATION_JSON_UTF8);
        response.setCharacterEncoding(APPLICATION_ENCODING);
        response.setStatus(statusCode);

        out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }
}
