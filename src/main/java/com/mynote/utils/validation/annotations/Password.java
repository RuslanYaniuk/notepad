package com.mynote.utils.validation.annotations;

import com.mynote.utils.validation.PasswordValidator;
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
 * @date October 2015
 */
@NotBlank
@Length(min = 8, max = 100)
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "does not meet minimum complexity";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
