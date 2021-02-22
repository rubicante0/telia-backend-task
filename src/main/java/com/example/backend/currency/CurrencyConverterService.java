package com.example.backend.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CurrencyConverterService {
    private final CurrencyConverterClient client;
    private final Clock clock;

    @Autowired
    public CurrencyConverterService(CurrencyConverterClient client) {
        this.client = client;
        this.clock = Clock.systemDefaultZone();
    }

    public BigDecimal convert(BigDecimal price, String currency) {
        if (currency.equals("EUR")) {
            return price;
        }

        BigDecimal rate = client.getRate(currency, LocalDate.now(clock));
        return price.multiply(rate);
    }
}
