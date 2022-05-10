package com.lucky.smartadplatform.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String username;

    private String email;

    private String password;

    private String validationPassword;

    private List<Item> items;

    private Set<Role> roles;

    public User(String username, String email, String password, String validationPassword,
            PasswordEncoder passwordEncoder) {
        validateUsername(username);
        this.username = username;
        validateEmail(email);
        this.email = email;
        validatePassword(password);
        validatePasswords(password, validationPassword);
        this.password = passwordEncoder.encode(password);
    }

    public User(String username, String email, String password, List<Item> items, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.items = items;
        this.setRoles(roles);
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    private void validateUsername(String username) {
        // TODO
    }

    private void validateEmail(String email) {
        // TODO
    }

    private void validatePassword(String password) {
        // TODO
    }

    private void validatePasswords(String password, String validationPassword) {
        if (!password.equals(validationPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
    }

}
