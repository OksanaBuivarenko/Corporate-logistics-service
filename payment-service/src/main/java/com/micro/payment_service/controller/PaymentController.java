package com.micro.payment_service.controller;

import com.micro.payment_service.dto.BalanceRq;
import com.micro.payment_service.dto.BalanceRs;
import com.micro.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-service/api/v1")
@OpenAPIDefinition(info =
@Info(title = "Payment API", version = "1.0", description = "Documentation Payment API v1.0")
)
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payment")
    public BalanceRs getBalanceByUserId(@RequestHeader("X-auth-user-id") String userId) {
        return paymentService.getBalanceRsByUserId(Long.valueOf(userId));
    }

    @PostMapping("/payment")
    public BalanceRs changeBalance(@RequestBody BalanceRq balanceRq, @RequestHeader("X-auth-user-id") String userId) {
        return paymentService.changeBalance(balanceRq, Long.valueOf(userId));
    }
}