package com.dr3mro.Valhalla.Api.Server.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dr3mro.Valhalla.Api.Server.dto.UserResponse;
import com.dr3mro.Valhalla.Api.Server.exceptions.DuplicateEmailException;
// removed InvalidPasswordException (validation handled by @Valid)
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

    public User createUser(User user) {
        // normalize email
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }

        // check duplicate
        if (user.getEmail() != null && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }

        if (user.getPassword() != null) {
            // assume DTO validation already enforced password constraints (@Valid + @ValidPassword)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        log.info("User created: {}", user.getEmail());
        return savedUser;
    }

    // password strength/validation is enforced via Bean Validation (@ValidPassword)
    public UserResponse getUser(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            return response;
        }).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public void updateUser(UserResponse user) {

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
                throw new DuplicateEmailException(normalized);
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
