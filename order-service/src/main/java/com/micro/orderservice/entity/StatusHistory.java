package com.micro.orderservice.entity;

import com.micro.starter.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "status_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order order;
}