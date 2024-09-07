package com.invoice.mangment.Controller;


import com.invoice.mangment.Entities.Users;
import com.invoice.mangment.Services.UserService;
import com.invoice.mangment.enums.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        Users registeredUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/createSubAdmin")
    public ResponseEntity<String> createSubAdmin(@RequestParam String username,
                                                 @RequestParam String password,
                                                 @RequestParam String email) {
        try {
            Users subAdmin = userService.createSubAdmin(username, password, email);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sub-admin created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating sub-admin: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/addPrivileges")
    public ResponseEntity<String> addPrivileges(@PathVariable Long userId, @RequestBody Set<Privilege> privileges) {
        userService.addPrivileges(userId, privileges);
        return ResponseEntity.ok("Privileges added successfully");
    }

    @PostMapping("/{userId}/removePrivileges")
    public ResponseEntity<String> removePrivileges(@PathVariable Long userId, @RequestBody Set<Privilege> privileges) {
        userService.removePrivileges(userId, privileges);
        return ResponseEntity.ok("Privileges removed successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        Optional<Users> userOpt = userService.getUserById(userId);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        Optional<Users> userOpt = userService.getUserByUsername(username);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateUserCredentials(@RequestParam String username,
                                                          @RequestParam String password) {
        boolean isValid = userService.validateUserCredentials(username, password);
        return isValid ? ResponseEntity.ok("User credentials are valid")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
