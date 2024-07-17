package com.micro.orderservice.service;

import com.micro.starter.dto.OrderKafkaDto;

public interface KafkaService {

    void produce(OrderKafkaDto orderKafkaDto);
}