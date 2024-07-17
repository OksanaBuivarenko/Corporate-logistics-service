package com.micro.delivery_service.service.impl;

import com.micro.delivery_service.entity.Delivery;
import com.micro.delivery_service.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryServiceImplTest {

    private final DeliveryRepository deliveryRepository = Mockito.mock(DeliveryRepository.class);
    private final DeliveryServiceImpl deliveryService = new DeliveryServiceImpl(deliveryRepository);

    @Test
    void createDelivery() {
        Delivery delivery = new Delivery();
        delivery.setOrderId(1L);
        delivery.setReceiverName("Name");
        delivery.setDestinationAddress("destinationAddress");
        delivery.setStatus(true);

        when(deliveryRepository.save(delivery)).thenReturn(delivery);
        Delivery result = deliveryService.createDelivery(1L, "Name",
                "destinationAddress");
        assertEquals(true, result.getStatus());
    }
}