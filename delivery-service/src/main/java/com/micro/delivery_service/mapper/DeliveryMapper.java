package com.micro.delivery_service.mapper;

import com.micro.delivery_service.dto.DeliveryRs;
import com.micro.delivery_service.entity.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {

    DeliveryMapper INSTANCE = getMapper(DeliveryMapper.class);

    DeliveryRs toDto(Delivery delivery);

}
