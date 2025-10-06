package com.dr3mro.Valhalla.Api.Server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import com.dr3mro.Valhalla.Api.Server.dto.UserResponse;
import com.dr3mro.Valhalla.Api.Server.dto.UserUpdateRequest;
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

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

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
        UserResponse user = new UserResponse();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.listUsers()).thenReturn(Collections.singletonList(user));

        List<UserResponse> users = usersController.ListUsers();

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
    void getUser() {
        UserResponse user = new UserResponse();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.getUser(any(UUID.class))).thenReturn(user);

        UserResponse returnedUser = usersController.GetUser(UUID.randomUUID().toString());

        assertEquals(user.getName(), returnedUser.getName());
        assertEquals(user.getEmail(), returnedUser.getEmail());
        verify(userService, times(1)).getUser(any(UUID.class));
    }

    @Test
    void updateUser() {
        UserUpdateRequest userRequest = new UserUpdateRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");

        UserResponse user = new UserResponse();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.updateUser(any(User.class))).thenReturn(user);

        usersController.UpdateUser(UUID.randomUUID().toString(), userRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).updateUser(captor.capture());
        User passed = captor.getValue();
        assertEquals(userRequest.getName(), passed.getName());
        assertEquals(userRequest.getEmail(), passed.getEmail());
    }

}
