package com.mynote.utils.validation.annotations;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
@NotBlank
@Length(min = 5, max = 191)
@org.hibernate.validator.constraints.Email(
        regexp = "(?i)^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$"
)
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Email {

    String message() default "not well formed email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
