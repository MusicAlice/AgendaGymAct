package io.bootify.agenda_gym.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {

    private static final Set<String> COMMON_WEAK = Set.of(
            "123456","12345678","password","qwerty","abc123","111111","1234567890",
            "iloveyou","admin","welcome","monkey","000000","password1"
    );

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        if (password == null) {
            buildMessage(context, "La contraseña es obligatoria");
            return false;
        }

        String pwd = password.trim();

        if (pwd.length() < 12) {
            buildMessage(context, "La contraseña debe tener al menos 12 caracteres");
            return false;
        }
        if (!pwd.chars().anyMatch(Character::isUpperCase)) {
            buildMessage(context, "La contraseña debe contener al menos una letra MAYÚSCULA");
            return false;
        }
        if (!pwd.chars().anyMatch(Character::isLowerCase)) {
            buildMessage(context, "La contraseña debe contener al menos una letra minúscula");
            return false;
        }
        if (!pwd.chars().anyMatch(Character::isDigit)) {
            buildMessage(context, "La contraseña debe contener al menos un número");
            return false;
        }
        if (!pwd.matches(".*[!@#$%^&*(),.?\":{}|<>_\\-\\[\\]\\\\/~`+=;:].*")) {
            buildMessage(context, "La contraseña debe contener al menos un símbolo (ej. !@#$%)");
            return false;
        }
        if (COMMON_WEAK.contains(pwd.toLowerCase())) {
            buildMessage(context, "La contraseña es demasiado común");
            return false;
        }
        // secuencias simples (ej. 1111, abc)
        if (pwd.matches(".*(\\d)\\1\\1.*") || pwd.matches("(?i).*(abc|def|qwe|asd|zxc|1234|4321|abcd).*")) {
            buildMessage(context, "Contraseña parece una secuencia simple");
            return false;
        }
        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}