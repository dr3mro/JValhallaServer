package com.dr3mro.Valhalla.Api.Server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dr3mro.Valhalla.Api.Server.dto.UserRequest;
import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.services.UserService;

class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        usersController.CreateUser(userRequest);

        verify(userService, times(1)).createUser(user);
    }

    @Test
    void listUsers() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.listUsers()).thenReturn(Collections.singletonList(user));

        List<User> users = usersController.ListUsers();

        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getName());
        verify(userService, times(1)).listUsers();
    }
}
