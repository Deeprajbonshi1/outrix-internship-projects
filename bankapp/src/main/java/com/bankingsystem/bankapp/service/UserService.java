package com.bankingsystem.bankapp.service;

import com.bankingsystem.bankapp.JWTConfiguration.JwtService;
import com.bankingsystem.bankapp.dto.AuthRequest;
import com.bankingsystem.bankapp.entity.User;
import com.bankingsystem.bankapp.exception.AuthenticationException;
import com.bankingsystem.bankapp.exception.UserAlreadyExistsException;
import com.bankingsystem.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;

    public String register(User user) {
        if (userRepo.existsByUsername(user.getUsername()))
            throw new UserAlreadyExistsException("Username already in use");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }

    public Map<String, String> login(AuthRequest req) {
        // authenticate via Spring Security
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        // generate JWT
        String token =(jwtService.generateToken(req.getUsername()));
        return Map.of("token", token);
    }
}