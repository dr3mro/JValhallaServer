package com.dr3mro.Valhalla.Api.Server.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dr3mro.Valhalla.Api.Server.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    @Override
    void deleteById(UUID userId);

}
