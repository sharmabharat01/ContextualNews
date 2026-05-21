package com.news.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.news.entity.NewsArticle;
import com.news.enums.EnrichmentStatus;
import com.news.repository.NewsRepository;
import com.news.service.LLMEnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LLMEnrichmentServiceImpl
        implements LLMEnrichmentService {

    private final WebClient.Builder webClientBuilder;

    private final NewsRepository repository;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.base-url}")
    private String baseUrl;

    @Override
    public String generateSummary(
            String title,
            String description
    ) {

        try {

            WebClient webClient =
                    webClientBuilder
                            .baseUrl(baseUrl)
                            .build();

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content",
                                    "Summarize this news in 2 concise lines:\n\n"
                                            + "Title: " + title
                                            + "\nDescription: " + description
                            )
                    )
            );

            JsonNode response =
                    webClient.post()
                            .uri("/chat/completions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(
                                    "Authorization",
                                    "Bearer " + apiKey
                            )
                            .header(
                                    "HTTP-Referer",
                                    "http://localhost:8080"
                            )
                            .header(
                                    "X-Title",
                                    "Contextual News App"
                            )
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block();

            if (response == null) {
                return null;
            }

            return response
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {

            log.error(
                    "Failed to generate summary",
                    e
            );

            return null;
        }
    }

    @Override
    @Async("llmTaskExecutor")
    public void enrichArticleAsync(
            NewsArticle article
    ) {

        try {
            Thread.sleep(100); // for ratelimitting as using free  openai

            String summary =
                    generateSummary(
                            article.getTitle(),
                            article.getDescription()
                    );

            article.setLlmSummary(summary);
            article.setEnrichmentStatus(EnrichmentStatus.COMPLETED);

            repository.save(article);

            log.info(
                    "LLM enrichment completed for article: {}",
                    article.getId()
            );

        } catch (Exception e) {
            article.setEnrichmentStatus(
                    EnrichmentStatus.FAILED
            );

            repository.save(article);

            log.error(
                    "Failed enrichment for article: {}",
                    article.getId(),
                    e
            );

            log.error(
                    "Failed to enrich article: {}",
                    article.getId(),
                    e
            );
        }
    }
}