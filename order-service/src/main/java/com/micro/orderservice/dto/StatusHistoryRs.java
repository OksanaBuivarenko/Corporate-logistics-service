package com.micro.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.micro.starter.dto.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusHistoryRs {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationTime;

    private OrderStatus status;

}