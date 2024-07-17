package com.micro.orderservice.service;
import com.micro.orderservice.dto.HistoryRs;
import com.micro.orderservice.dto.OrderRq;
import com.micro.orderservice.dto.OrderRs;
import com.micro.orderservice.entity.Order;
import com.micro.starter.dto.StatusDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> addOrder(OrderRq orderDto, Long userId, String authHeader);

    Boolean updateOrderStatus(Long id, StatusDto statusDto);

    List<OrderRs> getOrderList();

    OrderRs getOrderRsById(Long orderId);

    Order getOrderById(Long orderId);

    HistoryRs getStatusHistory(Long orderId);
}