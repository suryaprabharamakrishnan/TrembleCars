package com.trimbleCars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trimbleCars.model.Roles;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByNameIgnoreCase(String rolesName);

    boolean existsByNameIgnoreCase(String name);
    
}
