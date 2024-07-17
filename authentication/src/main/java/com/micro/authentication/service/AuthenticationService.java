package com.micro.authentication.service;

import com.micro.authentication.dto.JwtAuthenticationRs;
import com.micro.authentication.dto.RefreshTokenRq;
import com.micro.authentication.dto.SignInRq;
import com.micro.authentication.dto.SignUpRq;
import com.micro.authentication.entity.User;

public interface AuthenticationService {

    User signUp(SignUpRq signUpRq);

    JwtAuthenticationRs signin(SignInRq signInRq);

    JwtAuthenticationRs refreshToken(RefreshTokenRq refreshTokenRq);

    Object validateToken(String token);
}
