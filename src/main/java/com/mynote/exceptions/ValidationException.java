package com.mynote.exceptions;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -8647644934916997569L;

    private MessageSourceResolvable messageSourceResolvable;

    public ValidationException(FieldError fieldError) {
        super(fieldError.getCodes()[0]);
        this.messageSourceResolvable = fieldError;
    }

    public MessageSourceResolvable getMessageSourceResolvable() {
        return messageSourceResolvable;
    }

    public ValidationException(List<ObjectError> errorList) {
        super(getMessageAsErrorList(errorList));
    }

    public static String getMessageAsErrorList(List<ObjectError> errorList) {
        StringBuilder sb = new StringBuilder();

        for (ObjectError error : errorList) {
            sb.append(error.toString());
        }
        return sb.toString();
    }
}
