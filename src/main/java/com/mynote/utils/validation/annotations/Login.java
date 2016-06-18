package com.mynote.utils.validation.annotations;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
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
@Length(min = 8, max = 191)
@Pattern(regexp = "(?i)^[a-z0-9_-]{8,}$")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Login {

    String message() default "invalid login";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
