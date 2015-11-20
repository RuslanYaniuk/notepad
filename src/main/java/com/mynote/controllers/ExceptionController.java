package com.mynote.controllers;

import com.mynote.config.web.ExtendedMessageSource;
import com.mynote.dto.MessageDTO;
import com.mynote.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@ControllerAdvice
public class ExceptionController {

    @Autowired
    private ExtendedMessageSource messageSource;

    @ExceptionHandler({
            UserNotFoundException.class,
            UserRoleNotFoundException.class})
    public ResponseEntity handleNotFound(Exception e, Locale locale) {
        return createResponseEntity(e, locale, NOT_FOUND);
    }

    @ExceptionHandler({
            EmailAlreadyTakenException.class,
            LoginAlreadyTakenException.class,})
    public ResponseEntity handleBadRequest(Exception e, Locale locale) {
        return createResponseEntity(e, locale, BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException e, Locale locale) {
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setMessageCode(e.getMessage());
        messageDTO.setMessage(messageSource.getMessage(e.getMessageSourceResolvable(), locale));

        return new ResponseEntity<>(messageDTO, BAD_REQUEST);
    }

    @ExceptionHandler(OperationNotPermitted.class)
    public ResponseEntity handleForbidden(OperationNotPermitted e, Locale locale) {
        return createResponseEntity(e, locale, FORBIDDEN);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity handleInternalError(Exception e, Locale locale) {
        return createResponseEntity(e, locale, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SearchFieldsAreEmpty.class)
    public ResponseEntity handleSearchRequestError(Exception e, Locale locale) {
        return createResponseEntity(e, locale, NOT_ACCEPTABLE);
    }

    private ResponseEntity createResponseEntity(Exception e, Locale locale, HttpStatus httpStatus) {
        e.printStackTrace();
        return new ResponseEntity<>(messageSource.getMessageDTO(e.getMessage(), locale), httpStatus);
    }
}
