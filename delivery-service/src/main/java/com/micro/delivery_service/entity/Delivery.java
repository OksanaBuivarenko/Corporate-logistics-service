package com.micro.delivery_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "destination_address")
    private String destinationAddress;

    private Boolean status;
}
