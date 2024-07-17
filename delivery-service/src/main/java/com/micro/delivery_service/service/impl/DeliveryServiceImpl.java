package com.micro.delivery_service.service.impl;

import com.micro.delivery_service.dto.DeliveryRs;
import com.micro.delivery_service.entity.Delivery;
import com.micro.delivery_service.mapper.DeliveryMapper;
import com.micro.delivery_service.repository.DeliveryRepository;
import com.micro.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private int successDeliveryCounter = 1;

    @Override
    public List<DeliveryRs> getDeliveries() {
        return deliveryRepository.findAll().stream().map(DeliveryMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Delivery createDelivery(Long orderId, String receiverName, String destinationAddress) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setReceiverName(receiverName);
        delivery.setDestinationAddress(destinationAddress);
        delivery.setStatus(getStatus());
        return deliveryRepository.save(delivery);
    }

    private Boolean getStatus() {
        Boolean status = isSuccessNumber();
        if (successDeliveryCounter == 100) {
            successDeliveryCounter = 1;
        } else {
            successDeliveryCounter++;
        }
        return status;
    }

    private Boolean isSuccessNumber() {
        List<Integer> failedNumbers = new ArrayList<>(Arrays.asList(3, 10, 17, 21, 28, 35, 41, 48,
                56, 63, 70, 76, 81, 90, 99));
        for (Integer failedNumber : failedNumbers) {
            if (successDeliveryCounter == failedNumber) {
                return false;
            }
        }
        return true;
    }
}
