package com.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "llmTaskExecutor")
    public Executor llmTaskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(3); // using less thread pool as too many request at a time

        executor.setMaxPoolSize(3);

        executor.setQueueCapacity(2500); // since long queue then increased queue size but it can be less and we can increase thread if using premium version
        //performance is compromised as we needed rate limitter for llm calls

        executor.setThreadNamePrefix(
                "llm-worker-"
        );

        executor.initialize();

        return executor;
    }
}