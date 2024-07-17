package com.micro.inventory_service.service;

import com.micro.starter.dto.OrderStatus;
import com.micro.starter.dto.OrderKafkaDto;
import com.micro.inventory_service.entity.Product;
import com.micro.inventory_service.exception.NotEnoughProductException;
import com.micro.inventory_service.exception.ProductNotFoundException;
import com.micro.starter.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDtoListener {

    private final InventoryService inventoryService;

    private final KafkaService kafkaService;

    private final WebClientService webClientService;

    @Value("${spring.kafka.topic-invented}")
    private String kafkaTopicInvented;

    @Value("${spring.kafka.topic-inventory-failed}")
    private String kafkaTopicFailed;

    @KafkaListener(topics = "${spring.kafka.topic-paid}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderKafkaDto kafkaDto) throws InterruptedException {
        try {
            Map<Product, Integer> productList = inventoryService.getProductList(kafkaDto.getProductKafkaDTOList());
            Thread.sleep(5000);
            inventoryService.decreaseProduct(productList);
            webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), OrderStatus.invented);
            kafkaDto.setStatus("invented");
            kafkaService.produce(kafkaDto, kafkaTopicInvented);
        } catch (ProductNotFoundException | NotEnoughProductException ex) {
            inventoryRollbackInfo(kafkaDto, OrderStatus.inventment_failed, ex);
        } catch (Exception ex) {
            inventoryRollbackInfo(kafkaDto, OrderStatus.unexpected_failure, ex);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic-delivery-failed}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listenFailed(@Payload OrderKafkaDto kafkaDto) {
        Map<Product, Integer> productList = inventoryService.getProductList(kafkaDto.getProductKafkaDTOList());
        inventoryService.increaseProduct(productList);
        kafkaService.produce(kafkaDto, kafkaTopicFailed);
        log.info("Product by order with id" + kafkaDto.getOrderId() + " was returned to the stock.");
    }

    private void inventoryRollbackInfo(OrderKafkaDto kafkaDto, OrderStatus orderStatus, Exception ex) {
        webClientService.changeOrderStatus(kafkaDto.getOrderId(), kafkaDto.getAuthHeader(), orderStatus);
        kafkaDto.setStatus(orderStatus.toString());
        log.info("Inventment with order id " + kafkaDto.getOrderId() + " failed");
        if(ex != null) {
            log.info(ex.getMessage());
        }
        kafkaService.produce(kafkaDto, kafkaTopicFailed);
    }
}