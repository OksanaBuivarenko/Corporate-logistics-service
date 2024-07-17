package com.micro.authentication.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignUpRq {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
