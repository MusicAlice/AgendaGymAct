package io.bootify.agenda_gym.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameConstraintValidator implements ConstraintValidator<NameConstraint, String> {

    private static final String ALLOWED_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s']+$";

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        String v = value.trim();
        if (v.isEmpty()) return false;

        if (v.length() < 3 || v.length() > 30) {
            buildMessage(context, "El nombre debe tener entre 3 y 30 caracteres");
            return false;
        }

        if (!v.matches(ALLOWED_REGEX)) {
            buildMessage(context, "El nombre solo puede contener letras, espacios y apóstrofes");
            return false;
        }

        String[] palabras = v.split("\\s+");
        for (String p : palabras) {
            if (p.length() > 0 && p.length() < 3) {
                buildMessage(context, "Cada palabra del nombre debe tener al menos 3 letras");
                return false;
            }
            // no más de dos letras iguales seguidas
            if (p.matches(".*(.)\\1\\1.*")) {
                buildMessage(context, "Las palabras no pueden tener más de dos letras iguales seguidas");
                return false;
            }
        }

        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}