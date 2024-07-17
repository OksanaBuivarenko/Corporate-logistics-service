package com.micro.orderservice.dto;

import com.micro.orderservice.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderRq {

    private String description;

    private List<Product> productList;

    private String receiverName;

    private String destinationAddress;

    private Integer cost;

}
