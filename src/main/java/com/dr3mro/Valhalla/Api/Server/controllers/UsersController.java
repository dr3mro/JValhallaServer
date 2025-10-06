package com.dr3mro.Valhalla.Api.Server.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dr3mro.Valhalla.Api.Server.dto.UserCreateRequest;
import com.dr3mro.Valhalla.Api.Server.dto.UserResponse;
import com.dr3mro.Valhalla.Api.Server.dto.UserUpdateRequest;
import com.dr3mro.Valhalla.Api.Server.models.User;
import com.dr3mro.Valhalla.Api.Server.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse CreateUser(@Valid @RequestBody UserCreateRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        User createdUser = userService.createUser(user);

        return UserResponse.builder()
                .id(createdUser.getId())
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .build();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteUser(@PathVariable String userId) {
        userService.deleteUser(UUID.fromString(userId));
    }

    @GetMapping("/{userId}")
    public UserResponse GetUser(@PathVariable String userId) {
        return userService.getUser(UUID.fromString(userId));
    }

    @PutMapping("/{userId}")
    public UserResponse UpdateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest userRequest) {
        User user = User.builder()
                .id(UUID.fromString(userId))
                .name(userRequest.getName() != null ? userRequest.getName().trim() : null)
                .email(userRequest.getEmail() != null ? userRequest.getEmail().trim().toLowerCase() : null)
                .password(userRequest.getPassword() != null ? userRequest.getPassword() : null)
                .build();

        return userService.updateUser(user);

    }

    // Should be removed or protected in production
    @GetMapping
    public List<User> ListUsers() {
        return userService.listUsers();
    }

}
