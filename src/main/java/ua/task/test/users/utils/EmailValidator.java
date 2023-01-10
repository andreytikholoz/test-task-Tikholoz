package ua.task.test.users.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String ADMIN_EMAIL = "admin";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);


    public static void validate(final String hex) {
        Matcher matcher = pattern.matcher(hex);
        if (!matcher.matches() && !hex.equals(ADMIN_EMAIL)) {
            throw new IllegalStateException("Invalid email format");
        }
    }
}
