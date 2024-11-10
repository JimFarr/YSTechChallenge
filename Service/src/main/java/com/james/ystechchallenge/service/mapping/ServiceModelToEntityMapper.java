package com.james.ystechchallenge.service.mapping;

import com.james.ystechchallenge.core.domain.Document;
import com.james.ystechchallenge.service.model.DocumentCreateModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceModelToEntityMapper {
    ServiceModelToEntityMapper INSTANCE = Mappers.getMapper(ServiceModelToEntityMapper.class);
    Document toEntity(DocumentCreateModel model);
}