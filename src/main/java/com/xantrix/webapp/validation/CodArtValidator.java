package com.xantrix.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CodArtValidator implements ConstraintValidator<CodArt, String> {
    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        // Verifica che il codice sia numerico
        if( value == null || !pattern.matcher(value).matches() ) {
            return false;
        }

        // Verifica che inizi con il valore 500
        if( !value.startsWith("500") ) {
            return false;
        }

        return true;
    }
}
