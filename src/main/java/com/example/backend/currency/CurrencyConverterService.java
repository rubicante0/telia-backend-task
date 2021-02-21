package com.example.backend.currency;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyConverterService {

    public BigDecimal convert(BigDecimal price, String currency) {
        if (!"EUR".equals(currency)) {
            throw new IllegalStateException(currency + " conversion is not implemented");
        }
        return price;
    }
}
