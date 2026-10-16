package com.marcos.anamnese.business;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagemService {

    private final ChatModel chatModel;

    private static final String PROMPT_INSTRUCAO = """
            Você é uma assistente especializada em Terapia Ocupacional Infantil trabalhando com a Terapeuta Larissa.
            Sua função é transcrever e estruturar as anotações destas imagens de avaliação / checklist de anamnese (que podem conter várias páginas sequenciais).

            DIRETRIZES FUNDAMENTAIS (ZERO ALUCINAÇÃO):
            1. FIDELIDADE ABSOLUTA: Transcreva rigorosamente apenas o que está marcado nos quadradinhos (checkboxes), preenchido ou escrito à mão/digitado nos documentos.
            2. NÃO INVENTE: NUNCA deduza, nunca presuma e nunca invente informações, hipóteses diagnósticas ou comportamentos que não estejam explicitamente no documento.
            3. OMISSÃO DE VAZIOS: Se um campo do cabeçalho ou item do checklist estiver em branco, desmarcado ou ilegível, OMITA-O completamente. NÃO escreva '[não especificado]', '[em branco]' ou similares; apenas liste os dados que realmente contêm informação preenchida.
            4. Se houver mais de uma página/imagem, analise todas em sequência e consolide os tópicos sem duplicar cabeçalhos.
            5. Estruture o texto de forma limpa e profissional, agrupando os tópicos preenchidos (ex: Dados Gerais, Histórico Clínico, Desenvolvimento Motor, Sensorial, AVDs, Rotina, Observações da Terapeuta).
            """;

    public String extrairTextoDeImagens(List<MultipartFile> arquivosImagem) throws IOException {
        List<Media> listaMedia = new ArrayList<>();

        for (MultipartFile arquivo : arquivosImagem) {
            String contentType = arquivo.getContentType() != null ? arquivo.getContentType() : "image/jpeg";
            MimeType mimeType = MimeType.valueOf(contentType);
            ByteArrayResource imagemResource = new ByteArrayResource(arquivo.getBytes());
            listaMedia.add(new Media(mimeType, imagemResource));
        }

        UserMessage userMessage = UserMessage.builder()
                .text(PROMPT_INSTRUCAO)
                .media(listaMedia)
                .build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();

        Prompt prompt = new Prompt(userMessage, options);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
