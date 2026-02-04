/* (C)2026 */
package com.ricardomartinso.todolist.domain.user;

import com.ricardomartinso.todolist.domain.user.exceptions.InvalidPasswordException;
import com.ricardomartinso.todolist.domain.user.vo.Email;
import com.ricardomartinso.todolist.domain.user.vo.Password;
import com.ricardomartinso.todolist.domain.user.vo.Role;
import com.ricardomartinso.todolist.domain.user.vo.UserId;
import com.ricardomartinso.todolist.domain.user.vo.Username;
import java.time.LocalDateTime;

public class User {
    private UserId id;
    private Username username;
    private Password password;
    private Email email;
    private Role role;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public User(UserId id, Username username, Password password, Email email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public void changeUsername(Username newUsername) {
        this.username = newUsername;
        this.update();
    }

    public boolean authenticate(String rawPassword, PasswordHasher hasher) {
        return password.matches(rawPassword, hasher);
    }

    public void changePassword(String currentPassword, String newPassword, PasswordHasher hasher) {
        if (!password.matches(currentPassword, hasher)) {
            throw new InvalidPasswordException("Current password is invalid");
        }
        this.password = Password.fromPlainText(newPassword, hasher);
        this.update();
    }

    public Email getEmail() {
        return email;
    }

    public void changeEmail(Email newEmail) {
        this.email = newEmail;
        this.update();
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getHashedPassword() {
        return password.value();
    }

    private void update() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reconstitutes a User from persistence layer data.
     * This method should only be used by infrastructure mappers.
     */
    public static User reconstitute(
            UserId id,
            Username username,
            Password password,
            Email email,
            Role role,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        User user = new User(id, username, password, email, role);
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        return user;
    }
}
