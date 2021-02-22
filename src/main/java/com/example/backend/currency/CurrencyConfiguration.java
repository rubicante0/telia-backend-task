package com.example.backend.currency;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(CurrencyConverterEndpointProperties.class)
public class CurrencyConfiguration {

    @Bean
    public RestTemplate restTemplate(CurrencyConverterEndpointProperties properties) {
        return new RestTemplateBuilder()
                .rootUri(properties.getEndpoint())
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }
}
