package com.micro.payment_service.service;

import com.micro.payment_service.dto.BalanceRq;
import com.micro.payment_service.dto.BalanceRs;
import com.micro.payment_service.entity.Balance;

public interface PaymentService {

    BalanceRs changeBalance(BalanceRq balanceRq, Long userId);

    BalanceRs getBalanceRsByUserId(Long userId);

    Balance getBalanceByUserId(Long userId);

    Boolean isEnoughCost(Integer orderCost, Long userId);

    Balance decreaseBalance(Integer orderCost, Long userId);

    Balance increaseBalance(Integer sum, Long userId);
}