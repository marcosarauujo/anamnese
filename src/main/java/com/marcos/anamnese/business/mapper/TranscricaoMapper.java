package com.marcos.anamnese.business.mapper;

import com.marcos.anamnese.business.dto.TranscricaoResponseDTO;
import com.marcos.anamnese.infrastructure.entitys.TranscricaoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranscricaoMapper {

    TranscricaoResponseDTO paraDTO(TranscricaoEntity entity);
}
