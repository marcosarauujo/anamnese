package com.marcos.anamnese.infrastructure.entitys;

import com.marcos.anamnese.infrastructure.enums.TipoOrigemTranscricao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transcricoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscricaoEntity {

    @Id
    private String id;
    private Long criancaId;
    private Long terapeutaId;
    private String textoBruto;
    private TipoOrigemTranscricao tipoOrigem;
    private LocalDateTime dataCriacao;

}
