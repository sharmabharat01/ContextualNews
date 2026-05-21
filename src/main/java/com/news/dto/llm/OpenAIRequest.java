package com.news.dto.llm;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OpenAIRequest {

    private String model;

    private List<Message> messages;
}