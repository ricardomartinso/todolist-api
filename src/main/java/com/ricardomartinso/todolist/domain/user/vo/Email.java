package com.ricardomartinso.todolist.domain.user.vo;

import java.util.Objects;

import com.ricardomartinso.todolist.domain.user.exceptions.InvalidEmailException;

public record Email(String value) {
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_LOCAL_PART_LENGTH = 64;

    public Email {
        validate(value);
    }

    private static void validateNotBlank(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email cannot be null or blank");
        }
    }

    private static void validateLength(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new InvalidEmailException(
                    "Email length exceeds maximum allowed length of 254 characters");
        }
    }

    private static void validateAtSymbol(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex < 1 || atIndex != email.lastIndexOf('@')) {
            throw new InvalidEmailException("Email must contain a single '@' character");
        }
    }

    private static void validateDomainPart(String domainPart) {
        if (domainPart.isBlank()) {
            throw new InvalidEmailException("Email domain cannot be empty");
        }

        if (!domainPart.matches("^[A-Za-z0-9.-]+$")) {
            throw new InvalidEmailException("Email domain contains invalid characters");
        }

        if (domainPart.startsWith("-") || domainPart.endsWith("-")) {
            throw new InvalidEmailException("Email domain cannot start or end with a hyphen");
        }

        if (domainPart.contains("..")) {
            throw new InvalidEmailException("Email domain cannot contain consecutive dots");
        }

        if (!domainPart.contains(".")) {
            throw new InvalidEmailException("Email domain must contain at least one dot");
        }

        validateTopLevelDomain(domainPart);
    }

    private static void validateTopLevelDomain(String domainPart) {
        String[] parts = domainPart.split("\\.");
        String tld = parts[parts.length - 1];

        if (tld.length() < 2) {
            throw new InvalidEmailException("Email top-level domain is invalid");
        }
    }

    private static void validateLocalPart(String localPart) {
        if (localPart.length() > MAX_LOCAL_PART_LENGTH) {
            throw new InvalidEmailException(
                    "Local part of the email exceeds maximum allowed length of 64 characters");
        }

        if (!localPart.matches("^[A-Za-z0-9._%+-]+$")) {
            throw new InvalidEmailException("Email local part contains invalid characters");
        }

        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            throw new InvalidEmailException("Email local part cannot start or end with a dot");
        }

        if (localPart.contains("..")) {
            throw new InvalidEmailException("Email local part cannot contain consecutive dots");
        }
    }

    private static String extractLocalPart(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    private static String extractDomainPart(String email) {
        return email.substring(email.indexOf('@') + 1);
    }

    private static void validate(String email) {
        validateNotBlank(email);
        validateLength(email);
        validateAtSymbol(email);

        String localPart = extractLocalPart(email);
        String domainPart = extractDomainPart(email);

        validateLocalPart(localPart);
        validateDomainPart(domainPart);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Email email))
            return false;
        return value.equalsIgnoreCase(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }

}
