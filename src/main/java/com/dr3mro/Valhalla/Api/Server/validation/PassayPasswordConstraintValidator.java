package com.dr3mro.Valhalla.Api.Server.validation;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.dr3mro.Valhalla.Api.Server.exceptions.InvalidPasswordException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PassayPasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private PasswordValidator validator;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        validator = new PasswordValidator(
                new LengthRule(8, 64),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, false),
                new WhitespaceRule()
        );
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException("Password cannot be null or blank");
        }

        RuleResult result = validator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }

        // build a single message
        String msg = String.join(", ", validator.getMessages(result));

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        return false;
    }

    // helper loading logic moved into initialize to avoid unused method warnings
}
