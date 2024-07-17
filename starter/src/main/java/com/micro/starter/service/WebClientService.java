package com.micro.starter.service;
import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.StatusDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientService {

    private static final String URL = "http://localhost:8765/order-service/api/v1/order/";

    public void changeOrderStatus(Long orderId, String authHeader, OrderStatus status){
        StatusDto statusDto = StatusDto.builder().status(status).build();
        WebClient webClient = WebClient.builder().build();
        webClient.patch()
                .uri(URL+orderId)
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(statusDto))
                .retrieve()
                .bodyToMono(StatusDto.class)
                .block();
    }
}
