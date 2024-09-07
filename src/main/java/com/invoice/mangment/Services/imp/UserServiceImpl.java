package com.invoice.mangment.Services.imp;

import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.Repository.UserRepository;
import com.invoice.mangment.Services.UserService;
import com.invoice.mangment.enums.Privilege;
import com.invoice.mangment.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Users registerUser(Users user) {
        user.setPassword(hashPassword(user.getPassword())); // Hash the password
        return userRepository.save(user);
    }

    @Override
    public Users createSubAdmin(String username, String password, String email) {
        Users subAdmin = new Users();
        subAdmin.setUsername(username);
        subAdmin.setPassword(hashPassword(password)); // Hash the password
        subAdmin.setEmail(email);
        subAdmin.setRoles(Set.of(Role.MANAGER)); // Example role, adjust as needed
        return userRepository.save(subAdmin);
    }

    @Override
    public void addPrivileges(Long userId, Set<Privilege> privileges) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            user.getPrivileges().addAll(privileges);
            userRepository.save(user);
        }
    }

    @Override
    public void removePrivileges(Long userId, Set<Privilege> privileges) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            user.getPrivileges().removeAll(privileges);
            userRepository.save(user);
        }
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean validateUserCredentials(String username, String password) {
        Optional<Users> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            // Compare the provided password (hashed) with the stored password hash
            return hashPassword(password).equals(user.getPassword());
        }
        return false;
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
