package com.micro.orderservice.mapper;

import com.micro.starter.dto.OrderKafkaDto;
import com.micro.orderservice.dto.OrderRs;
import com.micro.orderservice.entity.Order;
import com.micro.starter.dto.ProductKafkaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Map;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderMapper INSTANCE = getMapper(OrderMapper.class);

    OrderRs toDto(Order order);

    @Mapping(target = "authHeader", source = "authHeader")
    @Mapping(target = "productKafkaDTOList", source = "productKafkaDTOList")
    @Mapping(target = "userId", source = "order.userId")
    @Mapping(target = "description", source = "order.description")
    @Mapping(target = "receiverName", source = "order.receiverName")
    @Mapping(target = "destinationAddress", source = "order.destinationAddress")
    @Mapping(target = "bucketCost", source = "order.cost")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "creationTime", source = "order.creationTime")
    @Mapping(target = "modifiedTime", source = "order.modifiedTime")
    @Mapping(target = "orderId", source = "order.id")
    OrderKafkaDto toKafkaDto(Order order, String authHeader, List<ProductKafkaDTO> productKafkaDTOList);
}