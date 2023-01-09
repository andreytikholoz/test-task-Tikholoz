package ua.task.test.users.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private final Pattern pattern;
    private static final String ADMIN_EMAIL = "admin";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public void validate(final String hex) {
        Matcher matcher = pattern.matcher(hex);
        if (!matcher.matches() && !hex.equals(ADMIN_EMAIL)) {
            throw new IllegalStateException("Invalid email format");
        }
    }
}
