package com.jpmc.midascore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplate();
        // RestTemplate - Spring helper class for making HTTP requests
        // (like GET, POST, PUT, DELETE) to REST APIs.
    }
}
