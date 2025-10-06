package com.dr3mro.Valhalla.Api.Server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dr3mro.Valhalla.Api.Server.dto.UserCreateRequest;
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
        UserCreateRequest userRequest = new UserCreateRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");

        usersController.CreateUser(userRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).createUser(captor.capture());
        User passed = captor.getValue();
        assertEquals(userRequest.getName(), passed.getName());
        assertEquals(userRequest.getEmail(), passed.getEmail());
        assertEquals(userRequest.getPassword(), passed.getPassword());
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

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        usersController.DeleteUser(userId.toString());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void CreateUserThenDeleteUser() {
        UserCreateRequest userRequest = new UserCreateRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");

        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return null;
        }).when(userService).createUser(any(User.class));

        User createdUser = usersController.CreateUser(userRequest);
        // verify create call and the created user's fields
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).createUser(captor.capture());
        User passed = captor.getValue();
        assertEquals(userRequest.getEmail(), passed.getEmail());
        // also assert controller returned a user with same fields and an id was set
        assertEquals(userRequest.getEmail(), createdUser.getEmail());
        assertEquals(passed.getId(), createdUser.getId());

        // now delete: use a valid UUID as controller expects a UUID string and forwards UUID to service
        UUID userId = UUID.randomUUID();
        usersController.DeleteUser(userId.toString());
        verify(userService, times(1)).deleteUser(userId);
    }
}
