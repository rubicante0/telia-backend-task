package com.example.backend.currency;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class CurrencyRateResponse {
    private Map<String, BigDecimal> rates;
}
