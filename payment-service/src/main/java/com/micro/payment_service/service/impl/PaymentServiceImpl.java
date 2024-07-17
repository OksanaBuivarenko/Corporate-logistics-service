package com.micro.payment_service.service.impl;

import com.micro.payment_service.dto.BalanceRq;
import com.micro.payment_service.dto.BalanceRs;
import com.micro.payment_service.entity.Balance;
import com.micro.payment_service.mapper.BalanceMapper;
import com.micro.payment_service.repository.BalanceRepository;
import com.micro.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final BalanceRepository balanceRepository;

    @Override
    @Transactional
    public BalanceRs changeBalance(BalanceRq balanceRq, Long userId) {
        Balance balance = getBalanceByUserId(userId);
        balance.setBalance(balance.getBalance() + balanceRq.getSum());
        Balance changeBalance = balanceRepository.save(balance);
        return BalanceMapper.INSTANCE.toDto(changeBalance);
    }

    @Override
    public BalanceRs getBalanceRsByUserId(Long userId) {
        return BalanceMapper.INSTANCE.toDto(getBalanceByUserId(userId));
    }

    @Override
    public Balance getBalanceByUserId(Long userId) {
        Optional<Balance> optionalBalance = balanceRepository.findByUserId(userId);
        if (optionalBalance.isPresent()) {
            return optionalBalance.get();
        } else {
            Balance balance = new Balance();
            balance.setUserId(userId);
            balance.setBalance(0);
            return balanceRepository.save(balance);
        }
    }

    @Override
    public Boolean isEnoughCost(Integer orderCost, Long userId) {
        return getBalanceByUserId(userId).getBalance() >= orderCost;
    }

    @Override
    @Transactional
    public Balance decreaseBalance(Integer orderCost, Long userId) {
        Balance balance = getBalanceByUserId(userId);
        balance.setBalance(balance.getBalance() - orderCost);
        return balanceRepository.save(balance);
    }

    @Override
    @Transactional
    public Balance increaseBalance(Integer sum, Long userId) {
        Balance balance = getBalanceByUserId(userId);
        balance.setBalance(balance.getBalance() + sum);
        return balanceRepository.save(balance);
    }
}