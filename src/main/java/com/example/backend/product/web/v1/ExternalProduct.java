package com.example.backend.product.web.v1;

import com.example.backend.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ExternalProduct {
    private final Long id;
    private final String title;

    public static ExternalProduct from(Product product) {
        return ExternalProduct.builder()
                .withId(product.getId())
                .withTitle(product.getTitle())
                .build();
    }
}
