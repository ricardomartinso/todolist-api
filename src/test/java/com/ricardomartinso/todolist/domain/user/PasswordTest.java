package com.ricardomartinso.todolist.domain.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import com.ricardomartinso.todolist.domain.user.exceptions.InvalidPasswordException;
import com.ricardomartinso.todolist.domain.user.vo.Password;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PasswordTest {

    private static final String VALID_PASSWORD = "ValidPassword1!";
    private static final String HASHED_PASSWORD = "hashedPassword";

    @Mock
    private PasswordHasher hasher;

    @Test
    void shouldCreatePasswordFromValidPlainText() {
        // Arrange
        when(hasher.hash(VALID_PASSWORD)).thenReturn(HASHED_PASSWORD);
        // Act
        Password password = Password.fromPlainText(VALID_PASSWORD, hasher);
        // Assert
        verify(hasher).hash(VALID_PASSWORD);
        assertNotNull(password);
    }

    @Test
    void shouldMatchPasswordWithHasher() {
        // Arrange
        String rawPassword = VALID_PASSWORD;
        when(hasher.hash(rawPassword)).thenReturn(HASHED_PASSWORD);
        when(hasher.matches(rawPassword, HASHED_PASSWORD)).thenReturn(true);

        Password password = Password.fromPlainText(rawPassword, hasher);
        // Act
        boolean matches = password.matches(rawPassword, hasher);
        // Assert
        verify(hasher).matches(rawPassword, HASHED_PASSWORD);
        assertTrue(matches);
    }

    @Test
    void shouldThrowExceptionForNullPassword() {
        assertThrows(
                InvalidPasswordException.class,
                () -> Password.fromPlainText(null, hasher));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "   ", // blank
            "Ab1!", // too short
            "validpassword1!", // no uppercase
            "VALIDPASSWORD1!", // no lowercase
            "ValidPassword!", // no number
            "ValidPassword1", // no special char
            "Valid Password1!" // contains space
    })
    void shouldThrowExceptionForInvalidPasswords(String invalidPassword) {
        // Act & Assert
        assertThrows(
                InvalidPasswordException.class,
                () -> Password.fromPlainText(invalidPassword, hasher));
    }

}