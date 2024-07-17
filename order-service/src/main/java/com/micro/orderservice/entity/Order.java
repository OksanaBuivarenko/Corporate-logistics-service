package com.micro.orderservice.entity;

import com.micro.starter.dto.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private List<StatusHistory> statusHistoryList;

    @OneToMany
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private List<Product> productList;

    public Order(
            Long userId,
            String receiverName,
            String destinationAddress,
            String description,
            Integer cost,
            LocalDateTime creationTime,
            LocalDateTime modifiedTime,
            OrderStatus status
    ) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.destinationAddress = destinationAddress;
        this.description = description;
        this.cost = cost;
        this.creationTime = creationTime;
        this.modifiedTime = modifiedTime;
        this.status = status;
    }
}