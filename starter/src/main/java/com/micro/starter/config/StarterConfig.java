package com.micro.starter.config;

import com.micro.starter.service.WebClientService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class StarterConfig {

    @Bean
    public WebClientService webClientService() {
        return new WebClientService();
    }

}
