package com.micro.payment_service.service.impl;

import com.micro.payment_service.entity.Balance;
import com.micro.payment_service.repository.BalanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private final BalanceRepository balanceRepository = Mockito.mock(BalanceRepository.class);
    private final PaymentServiceImpl paymentService = new PaymentServiceImpl(balanceRepository);

    Balance balance;

    @BeforeEach
    void setUp() {
        balance = new Balance();
        balance.setUserId(1L);
        balance.setBalance(1000);
    }

    @AfterEach
    void tearDown() {
        balance = null;
    }

    @Test
    void getBalanceByUserId() {
        when(balanceRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(balance));
        Balance result = paymentService.getBalanceByUserId(1L);
        assertEquals(1000, result.getBalance());
        assertEquals(1, result.getUserId());
        verify(balanceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void isEnoughCost() {
        when(balanceRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(balance));
        Boolean resultTrue = paymentService.isEnoughCost(500,1l);
        Boolean resultFalse = paymentService.isEnoughCost(5000,1l);
        assertEquals(true, resultTrue);
        assertEquals(false, resultFalse);
        verify(balanceRepository, times(2)).findByUserId(1L);
    }

    @Test
    void decreaseBalance() {
        Balance balance1 = new Balance();
        balance = new Balance();
        balance.setUserId(1L);
        balance.setBalance(100);
        when(balanceRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(balance));
        when(balanceRepository.save(balance)).thenReturn(balance1);
        paymentService.decreaseBalance(900, 1L);
        verify(balanceRepository, times(1)).save(balance);
        verify(balanceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void increaseBalance() {
        Balance balance1 = new Balance();
        balance = new Balance();
        balance.setUserId(1L);
        balance.setBalance(1500);
        when(balanceRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(balance));
        when(balanceRepository.save(balance)).thenReturn(balance1);
        paymentService.increaseBalance(500, 1L);
        verify(balanceRepository, times(1)).save(balance);
        verify(balanceRepository, times(1)).findByUserId(1L);
    }
}