package com.micro.authentication.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignInRq {

    private String email;

    private String password;

}
