package com.invoice.mangment.Controller;
import com.invoice.mangment.Entities.Users;

import com.invoice.mangment.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Users login(@RequestParam String username, @RequestParam String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userService.getUserByUsername(username).orElse(null);
        } catch (AuthenticationException e) {
            return null;
        }
    }
}
