package com.micro.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class HistoryRs {

    private Long orderId;

    private List<StatusHistoryRs> statusHistory;

}