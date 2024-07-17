package com.micro.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.orderservice.config.TestConfig;
import com.micro.orderservice.dto.OrderRq;
import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.StatusDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/insert.sql")
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    ObjectMapper mapper = new ObjectMapper();

    @DisplayName("Get order list with status 200")
    @Test
    void listOrders() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/order-service/api/v1/order"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Get list status history with status 200")
    void listStatusHistory() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/order-service/api/v1/status-history/2"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.orderId").value(2))
                .andExpect(jsonPath("$.statusHistory[0].status").value("registered"))
                .andExpect(jsonPath("$.statusHistory[1].status").value("paid"));
    }

    @Test
    @DisplayName("Get list status history with status 404")
    void listStatusHistoryWithErrorId() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/order-service/api/v1/status-history/200"))
                .andDo(print())
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get order by id with status 200")
    void orderByIdWithStatus200() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/order-service/api/v1/order/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$.destinationAddress").value("address 1"));
    }

    @Test
    @DisplayName("Get order by id with status 404")
    void orderByIdWithStatus404() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/order-service/api/v1/order/100"))
                .andDo(print())
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @DisplayName("Create new order")
    void addOrder() throws Exception {

        OrderRq orderRq = OrderRq.builder()
                .cost(100)
                .destinationAddress("new address")
                .description("new description")
                .receiverName(" new name")
                .productList(new ArrayList<>())
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/order-service/api/v1/order")
                        .header("Authorization", "Authorization")
                        .header("X-auth-user-id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderRq)))
                .andDo(print())
                .andExpectAll(status().isCreated());
    }

    @SneakyThrows
    @Test
    void updateOrderStatus() {
        StatusDto statusDTO = StatusDto.builder().status(OrderStatus.delivered).build();

        this.mockMvc.perform(patch("http://localhost:" + port + "/order-service/api/v1/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(statusDTO)))
                .andDo(print())
                .andExpectAll(status().isOk());
    }
}