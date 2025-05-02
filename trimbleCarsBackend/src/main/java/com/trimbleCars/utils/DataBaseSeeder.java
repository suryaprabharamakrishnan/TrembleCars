package com.trimbleCars.utils;

import com.trimbleCars.model.Roles;
import com.trimbleCars.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataBaseSeeder {

    @Autowired
    private RolesRepository rolesRepository;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            System.out.println("Seeding database...");
            seedRoles();
        };
    }

    private void seedRoles() {
        List<String> roleNames = Arrays.asList("customer", "admin","car_owner");

        for (String name : roleNames) {
            if (!rolesRepository.existsByNameIgnoreCase(name)) {
                Roles role = new Roles();
                role.setName(name);
                rolesRepository.save(role);
            }
        }
    }

}
