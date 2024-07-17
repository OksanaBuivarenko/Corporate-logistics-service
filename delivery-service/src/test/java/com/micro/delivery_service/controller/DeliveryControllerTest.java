package com.micro.delivery_service.controller;

import com.micro.delivery_service.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
class DeliveryControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Get deliveries")
    void getDeliveries() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/delivery-service/api/v1/delivery"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}