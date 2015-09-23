package com.mynote.config.validation;

import com.mynote.config.validation.annotations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password password) {
    }

    /**
     * Four rules for a password. It should have at least one lower case letter,
     * one upper case letter, one digit and one special character like (!, @, #, etc.).
     * Does not support unicode.
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<PasswordRule> rules = new ArrayList<>(4);

        rules.add(new PasswordRule("[A-Z]", s));
        rules.add(new PasswordRule("[a-z]", s));
        rules.add(new PasswordRule("\\d", s));
        rules.add(new PasswordRule("[^a-zA-Z0-9 ]", s));

        for (PasswordRule rule : rules) {
            if (!rule.checkPass()) {
                return false;
            }
        }
        return true;
    }

    private static class PasswordRule {
        private Pattern pattern;
        private String password;

        public PasswordRule(String regex, String password) {
            this.pattern  = Pattern.compile(regex);
            this.password = password;
        }

        public boolean checkPass() {
            return pattern.matcher(password).find();
        }
    }
}
