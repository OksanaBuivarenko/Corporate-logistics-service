package com.micro.delivery_service.controller;

import com.micro.delivery_service.dto.DeliveryRs;
import com.micro.delivery_service.service.DeliveryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-service/api/v1")
@OpenAPIDefinition(info =
@Info(title = "Delivery API", version = "1.0", description = "Documentation Delivery API v1.0")
)
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/delivery")
    public List<DeliveryRs> getDeliveries() {
        return deliveryService.getDeliveries();
    }

}
