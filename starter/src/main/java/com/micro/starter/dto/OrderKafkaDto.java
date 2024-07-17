package com.micro.starter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderKafkaDto {

    private Long orderId;

    private Long userId;

    private String description;

    private String receiverName;

    private String destinationAddress;

    private Integer bucketCost;

    private String status;

    private String creationTime;

    private String modifiedTime;

    private String authHeader;

    List<ProductKafkaDTO> productKafkaDTOList;

}
