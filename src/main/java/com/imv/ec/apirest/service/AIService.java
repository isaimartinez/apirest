package com.imv.ec.apirest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class AIService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AIService() {
         Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("OPENAI_API_KEY");
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<String> summarizeText(String text) {
        String requestBody = "{ \"model\": \"gpt-4\", \"messages\": [ " +
                "{\"role\": \"user\", \"content\": [{\"type\": \"text\", \"text\": \"Haz un resumen de 100 caracteres del siguiente texto\\n" + text + "\"}]}, " +
                "{\"role\": \"assistant\", \"content\": [{\"type\": \"text\", \"text\": \"\"}]} " +
                "], \"temperature\": 1, \"max_tokens\": 256, \"top_p\": 1, \"frequency_penalty\": 0, \"presence_penalty\": 0 }";

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractText)
                .onErrorResume(e -> Mono.just("Error summarizing text: " + e.getMessage()));
    }

    public Mono<String> describeImage(String imageUrl) {
        String requestBody = "{ \"model\": \"gpt-4\", \"messages\": [ " +
                "{\"role\": \"user\", \"content\": [{\"type\": \"text\", \"text\": \"Describe this image: " + imageUrl + "\"}]}, " +
                "{\"role\": \"assistant\", \"content\": [{\"type\": \"text\", \"text\": \"\"}]} " +
                "], \"temperature\": 1, \"max_tokens\": 256, \"top_p\": 1, \"frequency_penalty\": 0, \"presence_penalty\": 0 }";

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractText)
                .onErrorResume(e -> Mono.just("Error describing image: " + e.getMessage()));
    }

    private String extractText(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}
