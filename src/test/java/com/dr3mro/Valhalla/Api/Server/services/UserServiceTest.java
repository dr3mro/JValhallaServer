package com.dr3mro.Valhalla.Api.Server.services;

import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.createUser(user);

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void listUsers() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.listUsers();

        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }
}
