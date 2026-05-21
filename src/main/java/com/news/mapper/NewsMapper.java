package com.news.mapper;

import com.news.dto.NewsResponseDto;
import com.news.entity.NewsArticle;

public class NewsMapper {

    public static NewsResponseDto toDto(NewsArticle article) {

        return NewsResponseDto.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .url(article.getUrl())
                .publicationDate(article.getPublicationDate())
                .sourceName(article.getSourceName())
                .category(article.getCategory())
                .relevanceScore(article.getRelevanceScore())
                .llmSummary(article.getLlmSummary())
                .latitude(article.getLatitude())
                .longitude(article.getLongitude())
                .build();
    }
}