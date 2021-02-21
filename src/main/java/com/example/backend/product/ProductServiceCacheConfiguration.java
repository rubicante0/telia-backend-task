package com.example.backend.product;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.backend.product.ProductConstants.CACHE_NAME;

@Configuration(proxyBeanMethods = false)
public class ProductServiceCacheConfiguration {

    @Bean
    public CacheManager productServiceCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME);
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder());
        return caffeineCacheManager;
    }
}
