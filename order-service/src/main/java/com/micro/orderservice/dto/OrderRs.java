package com.micro.orderservice.dto;

import com.micro.starter.dto.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.micro.orderservice.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRs {

    private Long id;

    private Long userId;

    private String receiverName;

    private String destinationAddress;

    private String description;

    private Integer cost;

    private List<ProductRs> productList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime modifiedTime;

    private OrderStatus status;

}