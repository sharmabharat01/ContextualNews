package com.news.service;

import com.news.entity.NewsArticle;

public interface LLMEnrichmentService {

    void enrichArticleAsync(
            NewsArticle article
    );

    String generateSummary(
            String title,
            String description
    );
}