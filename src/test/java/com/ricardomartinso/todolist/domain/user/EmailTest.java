package com.ricardomartinso.todolist.domain.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.ricardomartinso.todolist.domain.user.exceptions.InvalidEmailException;
import com.ricardomartinso.todolist.domain.user.vo.Email;

import org.junit.jupiter.api.Test;

class EmailTest {

    private static final String VALID_EMAIL = "user.test+1@email.com";

    private static final String EMAIL_WITHOUT_AT = "email.com";

    private static final String EMAIL_WITH_MULTIPLE_AT = "a@b@c.com";

    private static final String EMAIL_WITH_CONSECUTIVE_DOTS_LOCAL = "user..name@email.com";

    private static final String EMAIL_WITHOUT_DOMAIN_DOT = "user@email";

    private static final String EMAIL_WITH_SHORT_TLD = "user@email.c";

    private static final String BLANK_EMAIL = "   ";

    private static final String DOMAIN = "@email.com";

    private static final String LOCAL_PART_65_CHARS = "a".repeat(65);

    /* ===================== TESTS ===================== */

    @Test
    void shouldCreateValidEmail() {
        Email email = new Email(VALID_EMAIL);

        assertEquals(VALID_EMAIL, email.value());
    }

    @Test
    void shouldRejectNullEmail() {
        assertThrows(InvalidEmailException.class,
                () -> new Email(null));
    }

    @Test
    void shouldRejectBlankEmail() {
        assertThrows(InvalidEmailException.class,
                () -> new Email(BLANK_EMAIL));
    }

    @Test
    void shouldRejectEmailWithoutAtSymbol() {
        assertThrows(InvalidEmailException.class,
                () -> new Email(EMAIL_WITHOUT_AT));
    }

    @Test
    void shouldRejectEmailWithMultipleAtSymbols() {
        assertThrows(InvalidEmailException.class,
                () -> new Email(EMAIL_WITH_MULTIPLE_AT));
    }

    @Test
    void shouldRejectLocalPartLongerThan64Characters() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email(LOCAL_PART_65_CHARS + DOMAIN));
    }

    @Test
    void shouldRejectConsecutiveDotsInLocalPart() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email(EMAIL_WITH_CONSECUTIVE_DOTS_LOCAL));
    }

    @Test
    void shouldRejectDomainWithoutDot() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email(EMAIL_WITHOUT_DOMAIN_DOT));
    }

    @Test
    void shouldRejectShortTld() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email(EMAIL_WITH_SHORT_TLD));
    }

    @Test
    void shouldBeEqualIgnoringCase() {
        Email e1 = new Email("User@Email.com");
        Email e2 = new Email("user@email.com");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
