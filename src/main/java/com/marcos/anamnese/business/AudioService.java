package com.marcos.anamnese.business;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AudioService {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public String transcreverAudio(MultipartFile arquivoDeAudio) throws IOException {

        Resource audioResource = new ByteArrayResource(arquivoDeAudio.getBytes()) {
            @Override
            public String getFilename() {
                return arquivoDeAudio.getOriginalFilename();
            }
        };

        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .language("pt")
                .temperature(0.2f)
                .build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, options);
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);

        return response.getResult().getOutput();

    }

}
