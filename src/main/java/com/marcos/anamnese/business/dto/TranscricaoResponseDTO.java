package com.marcos.anamnese.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TranscricaoResponseDTO {
    private String id;
    private Long criancaId;
    private String textoBruto;
    private LocalDateTime dataCriacao;
}
