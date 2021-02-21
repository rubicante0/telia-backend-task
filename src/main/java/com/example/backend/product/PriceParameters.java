package com.example.backend.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class PriceParameters {
    private final Long productId;
    private final Integer commitmentInMonths;
    private final Integer returnAfterMonths;
    private final String currency;
}
