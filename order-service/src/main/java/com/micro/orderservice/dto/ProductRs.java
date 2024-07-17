package com.micro.orderservice.dto;

import lombok.Data;

@Data
public class ProductRs {

    private String name;

    private Integer count;

    private Integer price;
}