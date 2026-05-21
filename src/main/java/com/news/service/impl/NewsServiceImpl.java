package com.news.service.impl;

import com.news.dto.NewsResponseDto;
import com.news.entity.NewsArticle;
import com.news.mapper.NewsMapper;
import com.news.repository.NewsRepository;
import com.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl
        implements NewsService {

    private final NewsRepository repository;

    @Override
    public Page<NewsResponseDto> search(
            String query,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("relevanceScore")
                                .descending()
                );

        Page<NewsArticle> articles =
                repository.search(
                        query,
                        pageable
                );

        return articles.map(
                NewsMapper::toDto
        );
    }

    @Override
    public Page<NewsResponseDto> getBySource(
            String source,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return repository
                .findBySourceNameIgnoreCase(
                        source,
                        pageable
                )
                .map(NewsMapper::toDto);
    }

    @Override
    public Page<NewsResponseDto> getByScore(
            Double minScore,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return repository
                .findByScore(
                        minScore,
                        pageable
                )
                .map(NewsMapper::toDto);
    }
}