package com.micro.authentication.dto;

import lombok.Data;

@Data
public class JwtAuthenticationRs {

    private String token;

    private String refreshToken;

}
