package com.invoice.mangment.Services.imp;

import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.Repository.UserRepository;
import com.invoice.mangment.Services.UserService;
import com.invoice.mangment.enums.Privilege;
import com.invoice.mangment.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users registerUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        return userRepository.save(user);
    }


    @Override
    public Users createSubAdmin(String username, String password, String email) {
        Users subAdmin = new Users();
        subAdmin.setUsername(username);
        subAdmin.setPassword(passwordEncoder.encode(password)); // Hash the password
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
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
