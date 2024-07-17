package com.micro.payment_service.mapper;

import com.micro.payment_service.dto.BalanceRs;
import com.micro.payment_service.entity.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BalanceMapper {

    BalanceMapper INSTANCE = getMapper(BalanceMapper.class);

    BalanceRs toDto(Balance balance);
}
