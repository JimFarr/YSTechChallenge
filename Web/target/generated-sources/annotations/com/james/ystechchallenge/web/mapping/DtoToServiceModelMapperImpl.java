package com.james.ystechchallenge.web.mapping;

import com.james.ystechchallenge.core.enumeration.AccreditationRequestType;
import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.DocumentCreateModel;
import com.james.ystechchallenge.web.dto.AccreditationRequestCreateDto;
import com.james.ystechchallenge.web.dto.DocumentCreateDto;
import com.james.ystechchallenge.web.dto.OutcomeStatusDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-11T00:28:33+0100",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class DtoToServiceModelMapperImpl implements DtoToServiceModelMapper {

    @Override
    public AccreditationRequestCreateModel toCreateModel(AccreditationRequestCreateDto saveDto) {
        if ( saveDto == null ) {
            return null;
        }

        AccreditationRequestCreateModel accreditationRequestCreateModel = new AccreditationRequestCreateModel();

        accreditationRequestCreateModel.setUserId( saveDto.getUserId() );
        if ( saveDto.getAccreditationType() != null ) {
            accreditationRequestCreateModel.setAccreditationType( Enum.valueOf( AccreditationRequestType.class, saveDto.getAccreditationType() ) );
        }
        accreditationRequestCreateModel.setDocument( toCreateModel( saveDto.getDocument() ) );

        return accreditationRequestCreateModel;
    }

    @Override
    public DocumentCreateModel toCreateModel(DocumentCreateDto documentDto) {
        if ( documentDto == null ) {
            return null;
        }

        DocumentCreateModel documentCreateModel = new DocumentCreateModel();

        documentCreateModel.setFileType( documentDto.getMimeType() );
        documentCreateModel.setName( documentDto.getName() );
        documentCreateModel.setContent( documentDto.getContent() );

        return documentCreateModel;
    }

    @Override
    public AccreditationRequestStatusUpdateModel toUpdateModel(OutcomeStatusDto statusDto, UUID accreditationId) {
        if ( statusDto == null && accreditationId == null ) {
            return null;
        }

        UUID accreditationId1 = null;
        accreditationId1 = accreditationId;

        AccreditationStatusType updatedStatus = mapOutcome(statusDto.getOutcome());

        AccreditationRequestStatusUpdateModel accreditationRequestStatusUpdateModel = new AccreditationRequestStatusUpdateModel( accreditationId1, updatedStatus );

        return accreditationRequestStatusUpdateModel;
    }
}
