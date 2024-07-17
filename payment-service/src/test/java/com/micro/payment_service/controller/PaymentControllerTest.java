package com.micro.payment_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.payment_service.config.TestConfig;
import com.micro.payment_service.dto.BalanceRq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
class PaymentControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Get balance by user id with status 200")
    void getBalanceByUserId() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/payment-service/api/v1/payment")
                        .header("X-auth-user-id", "1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.balance").value(5000));
    }

    @Test
    @DisplayName("Get balance by user id with not created user")
    void getBalanceByUserIdWithStatus404() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/payment-service/api/v1/payment")
                        .header("X-auth-user-id", "1000"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userId").value(1000))
                .andExpect(jsonPath("$.balance").value(0));

    }

    @Test
    @DisplayName("Change balance with status 200")
    void changeBalance() throws Exception {
        BalanceRq balanceRq = BalanceRq.builder().sum(1000).build();

        this.mockMvc.perform(post("http://localhost:" + port + "/payment-service/api/v1/payment")
                        .header("X-auth-user-id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(balanceRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.balance").value(6000));
    }

    @Test
    @DisplayName("Change balance by user id with not created user")
    void changeBalanceByUserIdWithNotCreatedUser() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/payment-service/api/v1/payment")
                        .header("X-auth-user-id", "1000"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userId").value(1000))
                .andExpect(jsonPath("$.balance").value(0));
    }
}