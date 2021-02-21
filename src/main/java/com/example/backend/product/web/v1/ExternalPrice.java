package com.example.backend.product.web.v1;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(setterPrefix = "with")
public class ExternalPrice {
    private final BigDecimal value;
}
