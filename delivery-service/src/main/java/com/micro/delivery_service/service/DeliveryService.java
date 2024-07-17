package com.micro.delivery_service.service;

import com.micro.delivery_service.dto.DeliveryRs;
import com.micro.delivery_service.entity.Delivery;

import java.util.List;

public interface DeliveryService {

    List<DeliveryRs> getDeliveries();

    Delivery createDelivery(Long id, String toString, String destinationAddress);
}
