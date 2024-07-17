package com.micro.payment_service.service;

import com.micro.starter.dto.OrderKafkaDto;

public interface KafkaService {

    void produce(OrderKafkaDto orderKafkaDto, String kafkaTopic);
}