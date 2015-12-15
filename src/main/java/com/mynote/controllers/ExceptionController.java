package com.mynote.controllers;

import com.mynote.config.web.ExtendedMessageSource;
import com.mynote.dto.MessageDTO;
import com.mynote.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@ControllerAdvice
public class ExceptionController extends AbstractController {

    @Autowired
    private ExtendedMessageSource messageSource;

    @ExceptionHandler({
            UserNotFoundException.class,
            UserRoleNotFoundException.class,
            NoteNotFoundException.class})
    public ResponseEntity handleNotFound(Exception e) {
        return messageNotFound(e.getMessage());
    }

    @ExceptionHandler({
            EmailAlreadyTakenException.class,
            LoginAlreadyTakenException.class,})
    public ResponseEntity handleBadRequest(Exception e) {
        return messageBadRequest(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException e, Locale locale) {
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setMessageCode(e.getMessage());
        messageDTO.setMessage(messageSource.getMessage(e.getMessageSourceResolvable(), locale));

        return badRequest(messageDTO);
    }

    @ExceptionHandler(OperationNotPermitted.class)
    public ResponseEntity handleForbidden(OperationNotPermitted e) {
        return messageForbidden(e.getMessage());
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity handleInternalError(Exception e) {
        return messageInternalServerError(e.getMessage());
    }

    @ExceptionHandler(SearchFieldsAreEmpty.class)
    public ResponseEntity handleSearchRequestError(Exception e) {
        return messageNotAcceptable(e.getMessage());
    }
}
