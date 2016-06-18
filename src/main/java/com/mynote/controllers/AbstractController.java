package com.mynote.controllers;

import com.mynote.config.Constants;
import com.mynote.dto.MessageDTO;
import com.mynote.utils.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Component
public abstract class AbstractController {

    private static final HttpHeaders COMMON_HEADERS;

    static {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type", Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8.toString());
        COMMON_HEADERS = HttpHeaders.readOnlyHttpHeaders(httpHeaders);
    }

    @Autowired
    protected CustomMessageSource customMessageSource;

    public ResponseEntity messageOK(String messageCode, String... messageArgs) {
        return jsonResponse(OK, messageCode, messageArgs);
    }

    public <T> ResponseEntity ok(T body) {
        return jsonResponse(body, OK);
    }

    public <T> ResponseEntity badRequest(T body) {
        return jsonResponse(body, BAD_REQUEST);
    }

    public ResponseEntity messageForbidden(String messageCode, String... messageArgs) {
        return jsonResponse(FORBIDDEN, messageCode, messageArgs);
    }

    public ResponseEntity messageInternalServerError(String messageCode, String... messageArgs) {
        return jsonResponse(INTERNAL_SERVER_ERROR, messageCode, messageArgs);
    }

    public ResponseEntity messageNotAcceptable(String messageCode, String... messageArgs) {
        return jsonResponse(NOT_ACCEPTABLE, messageCode, messageArgs);
    }

    public ResponseEntity messageNotFound(String messageCode, String... messageArgs) {
        return jsonResponse(NOT_FOUND, messageCode, messageArgs);
    }

    private ResponseEntity jsonResponse(HttpStatus httpStatus, String messageCode, String... messageArgs) {
        Locale currentThreadLocale = LocaleContextHolder.getLocale();
        MessageDTO messageDTO = customMessageSource.getMessageDTO(messageCode, currentThreadLocale, messageArgs);

        return jsonResponse(messageDTO, httpStatus);
    }

    private <T> ResponseEntity jsonResponse(T body, HttpStatus httpStatus) {
        return new ResponseEntity<>(body, COMMON_HEADERS, httpStatus);
    }
}
