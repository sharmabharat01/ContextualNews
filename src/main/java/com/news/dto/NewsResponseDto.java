package com.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDto {

    private String title;

    private String description;

    private String url;

    private LocalDateTime publicationDate;

    private String sourceName;

    private List<String> category;

    private Double relevanceScore;

    private String llmSummary;

    private Double latitude;

    private Double longitude;
}