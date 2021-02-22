package com.example.backend.product.web.v1;

import com.example.backend.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ExternalProduct {
    @Schema(example = "1", description = "product identifier")
    private final Long id;

    @Schema(example = "Sony PS5", description = "product title")
    private final String title;

    public static ExternalProduct from(Product product) {
        return ExternalProduct.builder()
                .withId(product.getId())
                .withTitle(product.getTitle())
                .build();
    }
}
