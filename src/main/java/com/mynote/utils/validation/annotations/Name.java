package com.mynote.utils.validation.annotations;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

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
@Length(min = 1, max = 255)
@SafeHtml
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Name {

    String message() default "invalid name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
