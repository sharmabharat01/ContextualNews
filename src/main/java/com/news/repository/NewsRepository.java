package com.news.repository;

import com.news.entity.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository
        extends JpaRepository<NewsArticle, String> {

    Page<NewsArticle> findBySourceNameIgnoreCase(
            String sourceName,
            Pageable pageable
    );

    @Query("""
    SELECT n FROM NewsArticle n
    WHERE LOWER(n.title)
    LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(n.description)
    LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY n.relevanceScore DESC
    """)
    Page<NewsArticle> search(
            @Param("query") String query,
            Pageable pageable
    );

    @Query("""
    SELECT n FROM NewsArticle n
    WHERE n.relevanceScore >= :score
    ORDER BY n.relevanceScore DESC
    """)
    Page<NewsArticle> findByScore(
            @Param("score") Double score,
            Pageable pageable
    );
}