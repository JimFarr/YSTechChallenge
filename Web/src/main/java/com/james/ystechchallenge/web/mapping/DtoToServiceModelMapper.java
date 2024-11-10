package com.james.ystechchallenge.web.mapping;

import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.DocumentCreateModel;
import com.james.ystechchallenge.web.dto.AccreditationRequestCreateDto;
import com.james.ystechchallenge.web.dto.DocumentCreateDto;
import com.james.ystechchallenge.web.dto.OutcomeStatusDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoToServiceModelMapper {
    DtoToServiceModelMapper INSTANCE = Mappers.getMapper(DtoToServiceModelMapper.class);
    
    AccreditationRequestCreateModel toCreateModel(AccreditationRequestCreateDto saveDto);
    
    @Mapping(source = "mimeType", target = "fileType") // service layer has no concept of MIME - that's a web paradigm
    DocumentCreateModel toCreateModel(DocumentCreateDto documentDto);

    @Mapping(target = "updatedStatus", expression = "java(mapOutcome(statusDto.getOutcome()))")
    @Mapping(source = "accreditationId", target = "accreditationId")
    AccreditationRequestStatusUpdateModel toUpdateModel(OutcomeStatusDto statusDto, UUID accreditationId);

    default AccreditationStatusType mapOutcome(String outcome) {
        return AccreditationStatusType.valueOf(outcome);
    }
}