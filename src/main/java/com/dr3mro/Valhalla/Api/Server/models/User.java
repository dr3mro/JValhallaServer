package com.dr3mro.Valhalla.Api.Server.models;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotBlank
    @Length(min = 8, max = 100)
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    @NotBlank
    @ToString.Exclude
    private String password;
}
