package com.example.backend.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.backend.currency.CurrencyConstants.CURRENCY_CACHE_NAME;

@Component
@RequiredArgsConstructor
public class CurrencyConverterClient {
    private final RestTemplate template;

    @Cacheable(cacheNames = CURRENCY_CACHE_NAME)
    public BigDecimal getRate(String currency, LocalDate date) {
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("date", date);

        String uri = UriComponentsBuilder.fromUriString("/{date}")
                .queryParam("symbols", currency)
                .build()
                .toUriString();

        CurrencyRateResponse response = template.getForObject(uri, CurrencyRateResponse.class, pathVariables);
        return Optional.ofNullable(response)
                .map(CurrencyRateResponse::getRates)
                .map(rates -> rates.get(currency))
                .orElseThrow(() -> new IllegalStateException("Unexpected response structure"));
    }
}
