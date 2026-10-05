package com.marcos.anamnese.controller;

import com.marcos.anamnese.business.TranscricaoService;
import com.marcos.anamnese.infrastructure.entitys.TranscricaoEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transcricao")

@Tag(name = "Transcrição", description = "Endpoints para converter áudio da sessão em texto")
@SecurityRequirement(name = "BearerAuth")

public class TranscricaoController {

    private final TranscricaoService transcricaoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Gerar transcrição de áudio",
            description = "Envia um áudio, transcreve e salva no MongoDB")
    public ResponseEntity<String> transcreverESalvarAudio(@RequestParam("audio")MultipartFile audio,
                                                          @RequestParam("criancaId")Long criancaId,
                                                          @RequestParam("terapeutaId")Long terapeutaId,
                                                          @RequestHeader(name = "Authorization", required = false) String token) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                transcricaoService.processarESalvarAudio(audio, criancaId, terapeutaId, token));
    }

    @GetMapping("/crianca/{criancaId}")
    @Operation(summary = "Listar transcrições", description = "Busca os textos brutos das sessões de um paciente")
    public ResponseEntity<List<TranscricaoEntity>> listarPorCrianca(@PathVariable Long criancaId) {
        return ResponseEntity.ok(transcricaoService.buscarPorCrianca(criancaId));
    }

}
