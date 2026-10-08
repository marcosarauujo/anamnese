package com.marcos.anamnese.controller;

import com.marcos.anamnese.business.TranscricaoService;
import com.marcos.anamnese.business.dto.TranscricaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Transcricao", description = "Endpoints para converter audio da sessao em texto via IA (Whisper)")
@SecurityRequirement(name = "BearerAuth")
public class TranscricaoController {

    private final TranscricaoService transcricaoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Fazer upload e transcrever audio",
            description = "Recebe um arquivo de audio (MP3/WAV)"
    )
    @ApiResponse(responseCode = "201", description = "Audio transcrito e salvo com sucesso no banco de dados.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "500", description = "Erro ao processar o audio. Verifique se o arquivo esta no formato " +
            "correto e se a chave da OpenAI esta configurada.")

    public ResponseEntity<String> transcreverESalvarAudio(
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("criancaId") Long criancaId,
            @RequestParam("terapeutaId") Long terapeutaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                transcricaoService.processarESalvarAudio(audio, criancaId, terapeutaId, token));
    }

    @GetMapping("/crianca/{criancaId}")
    @Operation(
            summary = "Listar transcricoes de uma crianca",
            description = "Retorna todos os textos brutos das sessoes ja transcritas para um determinado paciente. "
    )
    @ApiResponse(responseCode = "200", description = "Lista de transcricoes retornada com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhuma transcricao encontrada para o ID de crianca informado.")

    public ResponseEntity<List<TranscricaoResponseDTO>> listarPorCrianca(@PathVariable Long criancaId) {
        return ResponseEntity.ok(transcricaoService.buscarPorCrianca(criancaId));
    }

}
