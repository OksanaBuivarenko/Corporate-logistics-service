package com.micro.orderservice.repository;

import com.micro.orderservice.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
}
