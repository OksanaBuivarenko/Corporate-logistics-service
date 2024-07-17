package com.micro.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.authentication.config.TestConfig;
import com.micro.authentication.dto.SignInRq;
import com.micro.authentication.dto.SignUpRq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert.sql")
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Signup test")
    void signup() throws Exception {
        SignUpRq signUpRq = SignUpRq.builder()
                .firstName("Name")
                .lastName("Suname")
                .email("1@1.ru")
                .password("password")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/auth/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.firstName").value("Name"));
    }

    @Test
    @DisplayName("Signin test")
    void signIn() throws Exception {
        SignInRq signInRq = SignInRq.builder()
                .email("user@mail.ru")
                .password("user1")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/auth/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signInRq)))
                .andDo(print())
                .andExpectAll(status().isOk());
    }
}