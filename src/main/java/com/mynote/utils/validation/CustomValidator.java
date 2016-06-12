package com.mynote.utils.validation;

import com.mynote.exceptions.ValidationException;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class CustomValidator extends OptionalValidatorFactoryBean {

    @Override
    public void validate(Object target, Errors errors) throws ValidationException {
        processConstraintViolations(super.validate(target, ValidationOrder.class), errors);

        if (errors.hasErrors()) {
            if (errors.getFieldError() != null) {
                throw new ValidationException(errors.getFieldError());
            } else {
                throw new ValidationException(errors.getAllErrors());
            }
        }
    }
}
