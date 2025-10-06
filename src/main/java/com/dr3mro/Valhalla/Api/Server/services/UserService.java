package com.dr3mro.Valhalla.Api.Server.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dr3mro.Valhalla.Api.Server.exceptions.DuplicateEmailException;
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

    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
