package com.news.controller;

import com.news.dto.NewsResponseDto;
import com.news.dto.request.SearchRequest;
import com.news.dto.response.ApiResponse;
import com.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/search")
    public ApiResponse<Page<NewsResponseDto>> search(
            @Valid
            @RequestBody
            SearchRequest request
    ) {

        log.info(
                "Search request received. query={}, page={}, size={}",
                request.getQuery(),
                request.getPage(),
                request.getSize()
        );

        Page<NewsResponseDto> result =
                newsService.search(
                        request.getQuery(),
                        request.getPage(),
                        request.getSize()
                );

        return ApiResponse
                .<Page<NewsResponseDto>>builder()
                .success(true)
                .message("News fetched successfully")
                .data(result)
                .build();
    }
}