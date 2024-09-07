package com.invoice.mangment.Initializer;

import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.Repository.UserRepository;
import com.invoice.mangment.enums.Privilege;
import com.invoice.mangment.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Create a default admin user
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(hashPassword("adminPassword")); // Hash the password using custom logic
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

    // Custom password hashing function using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Helper function to convert byte array to hex string
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
