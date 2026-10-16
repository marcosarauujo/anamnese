package com.marcos.anamnese.business;

import com.marcos.anamnese.business.dto.CriancaResponseDTO;
import com.marcos.anamnese.business.dto.TerapeutaResponseDTO;
import com.marcos.anamnese.business.dto.TranscricaoResponseDTO;
import com.marcos.anamnese.business.mapper.TranscricaoMapper;
import com.marcos.anamnese.infrastructure.client.CoreClient;
import com.marcos.anamnese.infrastructure.entitys.TranscricaoEntity;
import com.marcos.anamnese.infrastructure.enums.TipoOrigemTranscricao;
import com.marcos.anamnese.infrastructure.exceptions.ConflictException;
import com.marcos.anamnese.infrastructure.repository.TranscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranscricaoService {

    private final CoreClient coreClient;
    private final AudioService audioService;
    private final TranscricaoRepository transcricaoRepository;
    private final TranscricaoMapper transcricaoMapper;
    private final ImagemService imagemService;

    public String processarESalvarAudio(MultipartFile audio,
                                        Long criancaId,
                                        Long terapeutaId,
                                        String token) throws IOException {
        try {
            CriancaResponseDTO criancaResponseDTO = coreClient.buscarCriancaPorId(criancaId, token);
            TerapeutaResponseDTO terapeutaResponseDTO = coreClient.buscarTerapeutaPorId(terapeutaId, token);

            String textoTranscrito = audioService.transcreverAudio(audio);

            TranscricaoEntity transcricaoEntity = new TranscricaoEntity();
            transcricaoEntity.setCriancaId(criancaResponseDTO.getId());
            transcricaoEntity.setTerapeutaId(terapeutaResponseDTO.getId());
            transcricaoEntity.setTextoBruto(textoTranscrito);
            transcricaoEntity.setTipoOrigem(TipoOrigemTranscricao.AUDIO);
            transcricaoEntity.setDataCriacao(LocalDateTime.now());

            transcricaoRepository.save(transcricaoEntity);

            return "Transcrição salva com sucesso para o paciente " + criancaResponseDTO.getNomeCrianca();
        } catch (ConflictException e) {
            throw new ConflictException("Erro ao processar a transcrição " + e.getMessage());
        }
    }

    public List<TranscricaoResponseDTO> buscarPorCrianca(Long criancaId) {
        return transcricaoRepository.findByCriancaId(criancaId).stream()
                .map(transcricaoMapper::paraDTO)
                .collect(Collectors.toList());
    }

    public String processarESalvarImagens(List<MultipartFile> imagens,
                                          Long criancaId,
                                          Long terapeutaId,
                                          String token) throws IOException {
        try {
            CriancaResponseDTO criancaResponseDTO = coreClient.buscarCriancaPorId(criancaId, token);
            TerapeutaResponseDTO terapeutaResponseDTO = coreClient.buscarTerapeutaPorId(terapeutaId, token);

            String textoExtraido = imagemService.extrairTextoDeImagens(imagens);

            TranscricaoEntity transcricaoEntity = new TranscricaoEntity();
            transcricaoEntity.setCriancaId(criancaResponseDTO.getId());
            transcricaoEntity.setTerapeutaId(terapeutaResponseDTO.getId());
            transcricaoEntity.setTextoBruto(textoExtraido);
            transcricaoEntity.setTipoOrigem(TipoOrigemTranscricao.IMAGEM);
            transcricaoEntity.setDataCriacao(LocalDateTime.now());

            transcricaoRepository.save(transcricaoEntity);

            return "Checklist/páginas (" + imagens.size() + " imagem(ns)) processadas e salvas com sucesso para o paciente " + criancaResponseDTO.getNomeCrianca();
        } catch (ConflictException e) {
            throw new ConflictException("Erro ao processar imagens: " + e.getMessage());
        }
    }
}
