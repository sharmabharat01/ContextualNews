package com.news.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryAnalysisDto {

    private List<String> entities;

    private String intent;

    private String category;

    private String source;

    private String searchQuery;

    private Double latitude;

    private Double longitude;
}