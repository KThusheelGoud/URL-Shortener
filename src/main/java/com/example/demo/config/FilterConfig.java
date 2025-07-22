package com.example.demo.config;

import com.example.demo.middleware.LoggingFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public Filter loggingFilter() {
        return new LoggingFilter();
    }
}