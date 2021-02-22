package com.example.backend.product;

import com.example.backend.common.CacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.NoOpCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static com.example.backend.product.ProductConstants.CACHE_NAME;

@Configuration
public class ProductServiceCacheConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "product.cache.enabled", havingValue = "true")
    public static class CacheConfiguration {

        @Bean(name = "productCacheProperties")
        @ConfigurationProperties("product.cache")
        public CacheProperties productCacheProperties() {
            return new CacheProperties();
        }

        @Bean
        public CacheManager productServiceCacheManager() {
            CacheProperties properties = productCacheProperties();
            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME);
            caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                    .expireAfterAccess(properties.getDuration()));
            return caffeineCacheManager;
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "product.cache.enabled", havingValue = "false", matchIfMissing = true)
    public static class NoCacheConfiguration {
        @Bean
        public CacheManager productServiceCacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Collections.singletonList(new NoOpCache(CACHE_NAME)));
            return cacheManager;
        }
    }
}
