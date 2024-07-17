package com.micro.authentication.service.impl;

import com.micro.authentication.dto.*;
import com.micro.authentication.entity.Role;
import com.micro.authentication.entity.User;
import com.micro.authentication.mapper.UserMapper;
import com.micro.authentication.repository.UserRepository;
import com.micro.authentication.service.AuthenticationService;
import com.micro.authentication.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User signUp(SignUpRq signUpRq) {
        User user = UserMapper.INSTANCE.toEntity(signUpRq, Role.USER, passwordEncoder.encode(signUpRq.getPassword()));
        return userRepository.save(user);
    }

    @SneakyThrows
    public JwtAuthenticationRs signin(SignInRq signInRq) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRq.getEmail(),
                signInRq.getPassword()));

        User user = (User) userRepository.findByEmail(signInRq.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));

        JwtAuthenticationRs jwtAuthenticationRs = new JwtAuthenticationRs();
        jwtAuthenticationRs.setToken(jwtService.generateToken(user));
        jwtAuthenticationRs.setRefreshToken(jwtService.generateRefreshToken(new HashMap<>(), user));

        return jwtAuthenticationRs;
    }

    public JwtAuthenticationRs refreshToken(RefreshTokenRq refreshTokenRq) {
        String userEmail = jwtService.extractUserName(refreshTokenRq.getToken());
        User user = (User) userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRq.getToken(), user)) {
            JwtAuthenticationRs jwtAuthenticationRs = new JwtAuthenticationRs();
            jwtAuthenticationRs.setToken(jwtService.generateToken(user));
            jwtAuthenticationRs.setRefreshToken(refreshTokenRq.getToken());
            return jwtAuthenticationRs;
        }
        return null;
    }

    @Override
    public UserDto validateToken(String token) {
        String userEmail = jwtService.extractUserName(token);
        User user = (User) userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(token, user)) {
            return UserMapper.INSTANCE.toUserDto(user, token);
        }
        return null;
    }
}
