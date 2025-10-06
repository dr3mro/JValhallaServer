package com.dr3mro.Valhalla.Api.Server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dr3mro.Valhalla.Api.Server.dto.UserResponse;
import com.dr3mro.Valhalla.Api.Server.exceptions.DuplicateEmailException;
import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.repositories.UserRepository;

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
    void createUser_withDuplicateEmail_throwsDuplicateEmailException() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(true);

        Executable executable = () -> userService.createUser(user);

        assertThrows(DuplicateEmailException.class, executable);
        verify(userRepository, never()).save(user);
    }

    @Test
    void listUsers() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponse> users = userService.listUsers();

        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse updatedUser = userService.updateUser(user);

        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
