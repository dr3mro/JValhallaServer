package com.dr3mro.Valhalla.Api.Server.config;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.WhitespaceRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    // Password policy configuration
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 64;
    public static final boolean REQUIRE_UPPERCASE = true;
    public static final boolean REQUIRE_LOWERCASE = true;
    public static final boolean REQUIRE_DIGIT = true;
    public static final boolean REQUIRE_SPECIAL = true;
    public static final int MIN_STRENGTH_SCORE = 3;

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator(
                new LengthRule(MIN_LENGTH, MAX_LENGTH),
                new CharacterRule(EnglishCharacterData.UpperCase, REQUIRE_UPPERCASE ? 1 : 0),
                new CharacterRule(EnglishCharacterData.LowerCase, REQUIRE_LOWERCASE ? 1 : 0),
                new CharacterRule(EnglishCharacterData.Digit, REQUIRE_DIGIT ? 1 : 0),
                new CharacterRule(EnglishCharacterData.Special, REQUIRE_SPECIAL ? 1 : 0),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false),
                new WhitespaceRule()
        );
    }
}
