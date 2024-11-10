package com.james.ystechchallenge.service.mapping;

import com.james.ystechchallenge.core.domain.Document;
import com.james.ystechchallenge.service.model.DocumentCreateModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-11T00:28:28+0100",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ServiceModelToEntityMapperImpl implements ServiceModelToEntityMapper {

    @Override
    public Document toEntity(DocumentCreateModel model) {
        if ( model == null ) {
            return null;
        }

        String fileType = null;
        String content = null;

        fileType = model.getFileType();
        content = model.getContent();

        Document document = new Document( fileType, content );

        return document;
    }
}
