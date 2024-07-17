package com.micro.orderservice.mapper;

import com.micro.orderservice.dto.StatusHistoryRs;
import com.micro.orderservice.entity.StatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusHistoryMapper {

    StatusHistoryMapper INSTANCE = getMapper(StatusHistoryMapper.class);

    StatusHistoryRs toDto(StatusHistory statusHistory);
}
