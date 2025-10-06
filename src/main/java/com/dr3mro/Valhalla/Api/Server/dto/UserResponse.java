package com.dr3mro.Valhalla.Api.Server.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;

    @JsonIgnore
    private String password;
}
