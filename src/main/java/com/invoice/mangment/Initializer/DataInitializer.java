package com.invoice.mangment.Initializer;

import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.Repository.UserRepository;
import com.invoice.mangment.enums.Privilege;
import com.invoice.mangment.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                // Create a default admin user
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("adminPassword")); // Hash the password
                admin.setEmail("admin@example.com");
                admin.setRoles(Set.of(Role.ADMIN));
                admin.setPrivileges(Set.of(
                        Privilege.CREATE_INVOICE,
                        Privilege.READ_INVOICE,
                        Privilege.UPDATE_INVOICE,
                        Privilege.DELETE_INVOICE,
                        Privilege.CREATE_PRODUCT,
                        Privilege.READ_PRODUCT,
                        Privilege.UPDATE_PRODUCT,
                        Privilege.DELETE_PRODUCT
                ));

                userRepository.save(admin);
                System.out.println("Default admin user created.");
            }
        };
    }

}
