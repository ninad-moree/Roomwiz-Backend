package com.hotels.roomwiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotels.roomwiz.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);

    boolean existsByName(String role);
}
