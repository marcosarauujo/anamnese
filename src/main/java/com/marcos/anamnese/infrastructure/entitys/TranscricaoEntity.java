package com.marcos.anamnese.infrastructure.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document (collection = "transcricoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscricaoEntity {

    @Id
    private String id;
    private Long criancaId;
    private Long TerapeutaId;
    private String textoBruto;
    private LocalDateTime dataCriacao;

}
