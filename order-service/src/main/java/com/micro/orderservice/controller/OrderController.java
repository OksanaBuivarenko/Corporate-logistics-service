package com.micro.orderservice.controller;

import com.micro.orderservice.dto.HistoryRs;
import com.micro.starter.dto.StatusDto;
import com.micro.orderservice.dto.OrderRq;
import com.micro.orderservice.dto.OrderRs;
import com.micro.orderservice.entity.Order;
import com.micro.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service/api/v1")
@OpenAPIDefinition(info =
@Info(title = "Order API", version = "1.0", description = "Documentation Order API v1.0")
)
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public List<OrderRs> listOrders() {
        return orderService.getOrderList();
    }

    @GetMapping("/status-history/{orderId}")
    public HistoryRs listStatusHistory(@PathVariable Long orderId) {
        return orderService.getStatusHistory(orderId);
    }

    @GetMapping("/order/{orderId}")
    public OrderRs OrderById(@PathVariable Long orderId) {
        return orderService.getOrderRsById(orderId);
    }

    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderRq input, @RequestHeader("X-auth-user-id") String userId,
                                      @RequestHeader("Authorization") String authHeader) {
        Optional<Order> result = orderService.addOrder(input, Long.valueOf(userId), authHeader);
        return result.isPresent() ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    @PatchMapping("/order/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody StatusDto statusDto) {
        Boolean result = orderService.updateOrderStatus(orderId, statusDto);
        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}