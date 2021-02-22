package com.example.backend.product.web.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(setterPrefix = "with")
public class ExternalPrice {
    @Schema(example = "52.15", description = "calculated price value")
    private final BigDecimal value;
}
