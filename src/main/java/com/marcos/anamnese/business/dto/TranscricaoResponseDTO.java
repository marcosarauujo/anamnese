package com.marcos.anamnese.business.dto;

import com.marcos.anamnese.infrastructure.enums.TipoOrigemTranscricao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscricaoResponseDTO {
    private String id;
    private Long criancaId;
    private String textoBruto;
    private TipoOrigemTranscricao tipoOrigem;
    private LocalDateTime dataCriacao;
}
