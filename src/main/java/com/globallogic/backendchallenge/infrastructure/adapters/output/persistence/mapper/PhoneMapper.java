package com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.mapper;

import com.globallogic.backendchallenge.domain.dto.PhoneDto;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.PhoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id.number", source = "dto.number")
    @Mapping(target = "id.cityCode", source = "dto.cityCode")
    @Mapping(target = "id.countryCode", source = "dto.countryCode")
    PhoneEntity toEntity(PhoneDto dto);

    @Mapping(target = "number", source = "entity.id.number")
    @Mapping(target = "cityCode", source = "entity.id.cityCode")
    @Mapping(target = "countryCode", source = "entity.id.countryCode")
    PhoneDto toDto(PhoneEntity entity);
}
