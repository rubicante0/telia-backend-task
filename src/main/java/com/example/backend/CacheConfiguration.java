package com.example.backend;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    @Primary
    public CacheManager compositeCacheManager(List<CacheManager> cacheManagerList) {
        CompositeCacheManager result = new CompositeCacheManager();
        result.setCacheManagers(cacheManagerList);
        return result;
    }
}

