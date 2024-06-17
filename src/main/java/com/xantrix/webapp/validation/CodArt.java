package com.xantrix.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention( RUNTIME )
@Target( FIELD )
@Constraint( validatedBy = CodArtValidator.class )
public @interface CodArt
{
    String message() default "{CodArt.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
