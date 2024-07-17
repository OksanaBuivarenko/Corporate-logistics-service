package com.micro.orderservice.repository;

import com.micro.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAll();

    Optional<Order> findById(Long id);

}
