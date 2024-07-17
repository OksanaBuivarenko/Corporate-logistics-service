package com.micro.delivery_service.dto;

import lombok.Data;

@Data
public class DeliveryRs {

    private Long id;

    private Long orderId;

    private String receiverName;

    private String destinationAddress;

}
