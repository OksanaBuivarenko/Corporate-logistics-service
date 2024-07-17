package com.micro.orderservice.mapper;

import com.micro.orderservice.dto.ProductRs;
import com.micro.orderservice.entity.Product;
import com.micro.starter.dto.ProductKafkaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductMapper INSTANCE = getMapper(ProductMapper.class);

    ProductKafkaDTO toKafkaDto(Product product);

    ProductRs toDto(Product product);
}