package com.micro.orderservice.service.impl;

import com.micro.orderservice.controller.OrderNotFoundException;
import com.micro.orderservice.entity.Order;
import com.micro.orderservice.entity.StatusHistory;
import com.micro.orderservice.repository.OrderRepository;
import com.micro.orderservice.repository.ProductRepository;
import com.micro.orderservice.repository.StatusHistoryRepository;
import com.micro.orderservice.service.KafkaService;
import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.StatusDto;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final StatusHistoryRepository statusHistoryRepository = Mockito.mock(StatusHistoryRepository.class);
    private final KafkaService kafkaService = Mockito.mock(KafkaServiceImpl.class);

    private final OrderServiceImpl orderService = new OrderServiceImpl(orderRepository, productRepository,
            statusHistoryRepository, kafkaService);

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setDescription("description");
    }

    @AfterEach
    void tearDown() {
        order = null;
    }

    @Test
    @Description("Update order status")
    void updateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);
        StatusDto statusDto = new StatusDto(OrderStatus.delivered);
        StatusHistory newStatus = new StatusHistory();
        newStatus.setOrder(order);
        newStatus.setStatus(statusDto.getStatus());
        newStatus.setCreationTime(LocalDateTime.now());
        when(statusHistoryRepository.save(newStatus)).thenReturn(newStatus);
        assertEquals(true, orderService.updateOrderStatus(1L, statusDto));
    }

    @Test
    @Description("Update order status with exception")
    void updateOrderStatusWithException() {
        when(orderRepository.findById(1L)).thenThrow(new OrderNotFoundException(1L));
        when(orderRepository.save(order)).thenReturn(order);
        StatusDto statusDto = new StatusDto(OrderStatus.delivered);
        StatusHistory newStatus = new StatusHistory();
        newStatus.setOrder(order);
        newStatus.setStatus(statusDto.getStatus());
        newStatus.setCreationTime(LocalDateTime.now());
        when(statusHistoryRepository.save(newStatus)).thenReturn(newStatus);
        assertEquals(false, orderService.updateOrderStatus(1L, statusDto));
    }

    @Test
    @Description("Get order by id")
    void getOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        assertEquals("description", orderService.getOrderById(1L).getDescription());
        verify(orderRepository, times(1)).findById(1L);
    }
}