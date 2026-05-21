package com.news.service;

import com.news.dto.NewsResponseDto;
import org.springframework.data.domain.Page;

public interface NewsService {

    Page<NewsResponseDto> search(
            String query,
            int page,
            int size
    );

    Page<NewsResponseDto> getBySource(
            String source,
            int page,
            int size
    );

    Page<NewsResponseDto> getByScore(
            Double minScore,
            int page,
            int size
    );
}