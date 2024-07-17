package com.micro.authentication.controller;

import com.micro.authentication.dto.*;
import com.micro.authentication.entity.User;
import com.micro.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRq signUpRq) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRq));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationRs> signin(@RequestBody SignInRq signInRq) {
        return ResponseEntity.ok(authenticationService.signin(signInRq));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationRs> refresh(@RequestBody RefreshTokenRq refreshTokenRq) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRq));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserDto> validate(@RequestParam String token) {
        return ResponseEntity.ok((UserDto) authenticationService.validateToken(token));
    }
}
