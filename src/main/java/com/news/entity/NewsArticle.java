package com.news.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.enums.EnrichmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "news_article",
        indexes = {
                @Index(
                        name = "idx_source_name",
                        columnList = "source_name"
                ),
                @Index(
                        name = "idx_relevance_score",
                        columnList = "relevance_score"
                ),
                @Index(
                        name = "idx_publication_date",
                        columnList = "publication_date"
                )
        }
)
public class NewsArticle {

    @Id
    private String id;

    @Column(length = 1000)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 2000)
    private String url;

    private LocalDateTime publicationDate;

    private String sourceName;

    @ElementCollection
    @CollectionTable(
            name = "news_article_category",
            joinColumns = @JoinColumn(name = "news_article_id")
    )
    private List<String> category;

    @JsonProperty("relevance_score")
    private Double relevanceScore;

    @Enumerated(EnumType.STRING)
    private EnrichmentStatus enrichmentStatus;

    @Column(columnDefinition = "TEXT")
    private String llmSummary;

    private Double latitude;

    private Double longitude;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}