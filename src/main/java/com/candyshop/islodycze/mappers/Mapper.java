package com.candyshop.islodycze.mappers;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper<ENTITY, DTO> {

    public abstract ENTITY toEntity(final DTO dto);

    public abstract DTO toDto(final ENTITY entity);

    public List<ENTITY> toEntity(final List<DTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<DTO> toDto(final List<ENTITY> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
