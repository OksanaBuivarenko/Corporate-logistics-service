package com.micro.delivery_service.service;

import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.OrderKafkaDto;
import com.micro.delivery_service.entity.Delivery;
import com.micro.starter.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDtoListener {

    private final DeliveryService deliveryService;

    private final KafkaService kafkaService;

    private final WebClientService webClientService;

    @KafkaListener(topics = "${spring.kafka.topic-invented}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderKafkaDto kafkaDto) throws InterruptedException {
        try {
            Delivery delivery = deliveryService.createDelivery(kafkaDto.getOrderId(), kafkaDto.getUserId().toString(),
                    kafkaDto.getDestinationAddress());
            Thread.sleep(5000);
            if (delivery.getStatus()) {
                webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), OrderStatus.delivered);
                kafkaDto.setStatus("delivered");
            } else {
                deliveryRollback(kafkaDto, OrderStatus.delivery_failed, null);
            }
        } catch (Exception ex) {
            deliveryRollback(kafkaDto, OrderStatus.unexpected_failure, ex);
        }
    }

    private void deliveryRollback(OrderKafkaDto kafkaDto, OrderStatus orderStatus, Exception ex) {
        webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), orderStatus);
        kafkaDto.setStatus(orderStatus.toString());
        log.info("Deliver with order id " + kafkaDto.getOrderId() + " failed");
        if (ex != null) {
            log.info(ex.getMessage());
        }
        kafkaService.produce(kafkaDto);
    }
}
