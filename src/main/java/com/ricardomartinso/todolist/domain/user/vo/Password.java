package com.ricardomartinso.todolist.domain.user.vo;

import com.ricardomartinso.todolist.domain.user.PasswordHasher;
import com.ricardomartinso.todolist.domain.user.exceptions.InvalidPasswordException;

import java.util.Objects;

public final class Password {

    private final String hash;

    private Password(String hash) {
        this.hash = hash;
    }

    public static Password fromPlainText(String raw, PasswordHasher hasher) {
        validate(raw);
        Objects.requireNonNull(hasher, "PasswordHasher cannot be null");

        return new Password(hasher.hash(raw));
    }

    public boolean matches(String raw, PasswordHasher hasher) {
        Objects.requireNonNull(hasher, "PasswordHasher cannot be null");
        return hasher.matches(raw, this.hash);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException("Password cannot be null or blank");
        }

        if (value.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }

        if (!value.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        }

        if (!value.matches(".*[a-z].*")) {
            throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        }

        if (!value.matches(".*\\d.*")) {
            throw new InvalidPasswordException("Password must contain at least one number");
        }

        if (!value.matches(".*[^a-zA-Z0-9].*")) {
            throw new InvalidPasswordException("Password must contain at least one special character");
        }

        if (value.contains(" ")) {
            throw new InvalidPasswordException("Password must not contain spaces");
        }
    }
}
