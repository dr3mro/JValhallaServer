package com.dr3mro.Valhalla.Api.Server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
        log.info("User created: {}", user.getEmail());
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
