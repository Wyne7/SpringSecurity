package com.coder.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coder.security.config.JwtService;
import com.coder.security.dto.AuthenticationRequest;
import com.coder.security.dto.AuthenticationResponse;
import com.coder.security.dto.RegisterRequest;
import com.coder.security.model.User;
import com.coder.security.role.Role;
import com.coder.security.userRepository.UserRepostiroy;

@Service
public class AuthenticationService {

    private UserRepostiroy repository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepostiroy repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
    //    User user = new User();
    //    user.setName(request.getName());
    //    user.setEmail(request.getEmail());
    //    user.setPassword(passwordEncoder.encode(request.getPassword()));
    //    user.setRole(Role.USER);
    //    repostiroy.save(user);
    var user = User.builder()
               .name(request.getName())
               .email(request.getEmail())
               .password(passwordEncoder.encode(request.getPassword()))
               .phone(request.getPhone())
               .role(Role.USER)
               .build();
               repository.save(user);
       var jwtToken = jwtService.generateToken(user);
       return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
       return AuthenticationResponse.builder().token(jwtToken).build();
        
    }
    
}
