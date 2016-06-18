package com.mynote.controllers;

import com.mynote.dto.MessageDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
@ControllerAdvice
public class ExceptionController extends AbstractController {

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
    public ResponseEntity handleBadRequest(Exception e, Locale locale) {
        String message = customMessageSource.getMessage(e.getMessage(), null, locale);
        MessageDTO messageDTO = new MessageDTO();
        FieldError error;
        String fieldName;
        String errorCode;

        if (e.getClass() == EmailAlreadyTakenException.class) {
            fieldName = "email";
            errorCode = "TakenEmail";
        } else {
            fieldName = "login";
            errorCode = "TakenLogin";
        }
        error = new FieldError(UserRegistrationDTO.class.getSimpleName(),
                fieldName, null, false, new String[]{errorCode}, null, message);
        messageDTO.setType(e.getClass().getSimpleName());
        messageDTO.getErrors().add(error);
        return badRequest(messageDTO);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException e) {
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setErrors(e.getErrors().getAllErrors());
        messageDTO.setType(ValidationException.class.getSimpleName());
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
