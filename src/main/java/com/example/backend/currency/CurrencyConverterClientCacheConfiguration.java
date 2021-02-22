package com.example.backend.currency;

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

import static com.example.backend.currency.CurrencyConstants.CURRENCY_CACHE_NAME;

@Configuration
public class CurrencyConverterClientCacheConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "currency.cache.enabled", havingValue = "true")
    public static class CacheConfiguration {

        @Bean(name = "currencyCacheProperties")
        @ConfigurationProperties("currency.cache")
        public CacheProperties currencyCacheProperties() {
            return new CacheProperties();
        }

        @Bean
        public CacheManager currencyConverterClientCacheManager() {
            CacheProperties properties = currencyCacheProperties();
            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CURRENCY_CACHE_NAME);
            caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                    .expireAfterAccess(properties.getDuration()));
            return caffeineCacheManager;
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "currency.cache.enabled", havingValue = "false", matchIfMissing = true)
    public static class NoCacheConfiguration {
        @Bean
        public CacheManager currencyConverterClientCacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Collections.singletonList(new NoOpCache(CURRENCY_CACHE_NAME)));
            return cacheManager;
        }
    }
}
