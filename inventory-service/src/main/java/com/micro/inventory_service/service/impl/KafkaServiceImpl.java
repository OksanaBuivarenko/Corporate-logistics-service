package com.micro.inventory_service.service.impl;

import com.micro.starter.dto.OrderKafkaDto;
import com.micro.inventory_service.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final KafkaTemplate<Long, OrderKafkaDto> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, OrderKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(OrderKafkaDto orderKafkaDto, String kafkaTopic) {
        kafkaTemplate.send(kafkaTopic, orderKafkaDto);
        logger.info(String.format("Sent to Kafka -> %s", orderKafkaDto));
    }
}