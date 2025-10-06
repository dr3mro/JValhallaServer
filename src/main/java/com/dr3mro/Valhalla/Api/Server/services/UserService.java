package com.dr3mro.Valhalla.Api.Server.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dr3mro.Valhalla.Api.Server.exceptions.DuplicateEmailException;
import com.dr3mro.Valhalla.Api.Server.exceptions.UserNotFoundException;
import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(User user) {
        // normalize email
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }

        // check duplicate
        if (user.getEmail() != null && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
        log.info("User created: {}", user.getEmail());
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public void updateUser(User user) {

        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is required for update.");
        }

        // load existing entity to preserve non-updated fields and validation constraints
        java.util.UUID id = user.getId();
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        if (user.getName() != null) {
            existing.setName(user.getName().trim());
        }

        if (user.getEmail() != null) {
            String normalized = user.getEmail().trim().toLowerCase();
            // if the email changes, ensure it isn't taken by someone else
            if (!normalized.equals(existing.getEmail()) && userRepository.existsByEmailIgnoreCase(normalized)) {
                throw new com.dr3mro.Valhalla.Api.Server.exceptions.DuplicateEmailException(normalized);
            }
            existing.setEmail(normalized);
        }

        if (user.getPassword() != null) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existing);
        log.info("User updated: {}", existing.getEmail());
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
    }
}
