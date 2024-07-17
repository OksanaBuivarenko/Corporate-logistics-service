package com.micro.inventory_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.inventory_service.config.TestConfig;
import com.micro.inventory_service.dto.ProductRq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
class InventoryControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Get all products")
    void getAllProducts() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/inventory-service/api/v1/product"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Get product by id with status 200")
    void getProductByIdWithStatus200() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/inventory-service/api/v1/product/2"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.name").value("name2"));
    }

    @Test
    @DisplayName("Get product by id with status 404")
    void getProductByIdWithStatus404() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/inventory-service/api/v1/product/200"))
                .andDo(print())
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get product by name with status 200")
    void getProductByName() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/inventory-service/api/v1/product/name/name3"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.name").value("name3"));
    }

    @Test
    @DisplayName("Get product by name with status 404")
    void getProductByNameWithStatus404() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/inventory-service/api/v1/product/name/name200"))
                .andDo(print())
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @DisplayName("Add product")
    void addProduct() throws Exception {
        ProductRq productRq = ProductRq.builder()
                .name("name4")
                .price(1000)
                .count(500)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/inventory-service/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRq)))
                .andDo(print())
                .andExpectAll(status().isOk());
    }

    @Test
    void deleteProduct() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/inventory-service/api/v1/product/1"))
                .andDo(print())
                .andExpectAll(status().isOk());
    }
}