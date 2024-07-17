package com.micro.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductRq {

    private String name;

    private Integer count;

    private Integer price;
}
