package com.invoice.mangment.Services;

import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.enums.Privilege;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Users registerUser(Users user);
    Users createSubAdmin(String username, String password, String email);
    void addPrivileges(Long userId, Set<Privilege> privileges);
    void removePrivileges(Long userId, Set<Privilege> privileges);
    List<Users> getAllUsers();
    Optional<Users> getUserById(Long userId);
    Optional<Users> getUserByUsername(String username);
    boolean validateUserCredentials(String username, String password);
}
