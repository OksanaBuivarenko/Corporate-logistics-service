package com.micro.orderservice.service.impl;

import com.micro.orderservice.dto.HistoryRs;
import com.micro.orderservice.mapper.ProductMapper;
import com.micro.orderservice.mapper.StatusHistoryMapper;
import com.micro.starter.dto.ProductKafkaDTO;
import com.micro.starter.dto.StatusDto;
import com.micro.starter.dto.OrderStatus;
import com.micro.orderservice.controller.OrderNotFoundException;
import com.micro.orderservice.dto.OrderRq;
import com.micro.orderservice.dto.OrderRs;
import com.micro.orderservice.entity.Order;
import com.micro.orderservice.entity.Product;
import com.micro.orderservice.entity.StatusHistory;
import com.micro.orderservice.mapper.OrderMapper;
import com.micro.orderservice.repository.OrderRepository;
import com.micro.orderservice.repository.ProductRepository;
import com.micro.orderservice.repository.StatusHistoryRepository;
import com.micro.orderservice.service.KafkaService;
import com.micro.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final KafkaService kafkaService;

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderRq orderDto, Long userId, String authHeader) {
        List<ProductKafkaDTO> productKafkaDTOList = new ArrayList<>();

        LocalDateTime dateTime = LocalDateTime.now();
        Order newOrder = new Order(
                userId,
                orderDto.getReceiverName(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                dateTime,
                dateTime,
                OrderStatus.registered
        );

        Order order = orderRepository.save(newOrder);
        for (Product product : orderDto.getProductList()) {
            product.setOrder(order);
            productRepository.save(product);
            productKafkaDTOList.add(ProductMapper.INSTANCE.toKafkaDto(product));
        }

        StatusHistory newStatus = new StatusHistory();
        newStatus.setOrder(order);
        newStatus.setStatus(OrderStatus.registered);
        newStatus.setCreationTime(dateTime);
        statusHistoryRepository.save(newStatus);

        kafkaService.produce(OrderMapper.INSTANCE.toKafkaDto(order, authHeader, productKafkaDTOList));
        return Optional.of(order);
    }

    @Transactional
    @Override
    public Boolean updateOrderStatus(Long id, StatusDto statusDto) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
            order.setStatus(statusDto.getStatus());
            order.setModifiedTime(dateTime);
            Order resultOrder = orderRepository.save(order);
            StatusHistory newStatus = new StatusHistory();
            newStatus.setOrder(resultOrder);
            newStatus.setStatus(statusDto.getStatus());
            newStatus.setCreationTime(dateTime);
            statusHistoryRepository.save(newStatus);
            return true;
        } catch (RuntimeException e) {
            logger.error(String.format("Update status failed: %s", e.getMessage()));
            return false;
        }
    }

    @Override
    public List<OrderRs> getOrderList() {
        return orderRepository.findAll().stream().map(OrderMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderRs getOrderRsById(Long orderId) {
        return OrderMapper.INSTANCE.toDto(getOrderById(orderId));
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public HistoryRs getStatusHistory(Long orderId) {
        return HistoryRs.builder()
                .orderId(orderId)
                .statusHistory(getOrderById(orderId).getStatusHistoryList().stream()
                        .map(StatusHistoryMapper.INSTANCE::toDto).collect(Collectors.toList()))
                .build();
    }
}