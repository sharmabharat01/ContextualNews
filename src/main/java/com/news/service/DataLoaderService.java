package com.news.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.entity.NewsArticle;
import com.news.enums.EnrichmentStatus;
import com.news.repository.NewsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoaderService {

    private final NewsRepository repository;

    private final ObjectMapper objectMapper;

    private final LLMEnrichmentService llmService;

    private static final int BATCH_SIZE = 100;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void loadData() {

        try {

            if (repository.count() > 0) {

                log.info(
                        "News articles already loaded"
                );

                return;
            }

            log.info(
                    "Starting streamed news data loading"
            );

            InputStream inputStream =
                    new ClassPathResource(
                            "data/news.json"
                    ).getInputStream();

            JsonParser parser =
                    objectMapper.getFactory()
                            .createParser(inputStream);

            if (parser.nextToken() != JsonToken.START_ARRAY) {

                throw new RuntimeException(
                        "Expected JSON array"
                );
            }

            List<NewsArticle> batch =
                    new java.util.ArrayList<>();

            while (parser.nextToken() != JsonToken.END_ARRAY) {

                NewsArticle article =
                        objectMapper.readValue(
                                parser,
                                NewsArticle.class
                        );
                article.setEnrichmentStatus(EnrichmentStatus.PENDING);

                batch.add(article);

                if (batch.size() >= BATCH_SIZE) {

                    // processing batch
                    processBatch(batch);

                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {

                processBatch(batch);
            }

            log.info(
                    "Completed loading all news articles"
            );

        } catch (Exception e) {

            log.error(
                    "Failed to load news articles",
                    e
            );
        }
    }

    private void processBatch(
            List<NewsArticle> batch
    ) {

        repository.saveAll(batch);

        repository.flush();

        entityManager.clear();

        batch.forEach(
                llmService::enrichArticleAsync
        );

        log.info(
                "Processed batch of size: {}",
                batch.size()
        );
    }
}