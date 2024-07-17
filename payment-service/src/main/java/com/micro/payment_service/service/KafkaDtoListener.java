package com.micro.payment_service.service;

import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.OrderKafkaDto;
import com.micro.starter.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDtoListener {
    private final PaymentService paymentService;
    private final KafkaService kafkaService;
    private final WebClientService webClientService;

    @Value("${spring.kafka.topic-paid}")
    private String kafkaTopicPaid;

    @KafkaListener(topics = "${spring.kafka.topic}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderKafkaDto kafkaDto) throws InterruptedException {
        if (!paymentService.isEnoughCost(kafkaDto.getBucketCost(), kafkaDto.getUserId())) {
            paymentRollbackInfo(kafkaDto, OrderStatus.payment_failed, null);
        }
        else {
            Thread.sleep(5000);
            try {
                paymentService.decreaseBalance(kafkaDto.getBucketCost(), kafkaDto.getUserId());
                webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), OrderStatus.paid);
                kafkaDto.setStatus("paid");
                kafkaService.produce(kafkaDto, kafkaTopicPaid);
            } catch (Exception ex) {
                paymentRollbackInfo(kafkaDto, OrderStatus.unexpected_failure, ex);
            }
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic-inventory-failed}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listenFailed(@Payload OrderKafkaDto kafkaDto) {
        paymentService.increaseBalance(kafkaDto.getBucketCost(), kafkaDto.getUserId());
        log.info("Money in the amount " + kafkaDto.getBucketCost() + " was returned to the client  with id "
                + kafkaDto.getUserId());
    }

    public void paymentRollbackInfo(OrderKafkaDto kafkaDto, OrderStatus orderStatus, Exception ex) {
        webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), orderStatus);
        kafkaDto.setStatus(orderStatus.toString());
        log.info("Payment with id " + kafkaDto.getOrderId() + " failed");
        if(ex != null) {
            log.info(ex.getMessage());
        }
    }
}
