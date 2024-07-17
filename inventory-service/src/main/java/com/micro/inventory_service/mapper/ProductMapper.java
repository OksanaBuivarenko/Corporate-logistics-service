package com.micro.inventory_service.mapper;

import com.micro.inventory_service.dto.ProductRq;
import com.micro.inventory_service.dto.ProductRs;
import com.micro.inventory_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductMapper INSTANCE = getMapper(ProductMapper.class);

    ProductRs toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRq productRq);

}
