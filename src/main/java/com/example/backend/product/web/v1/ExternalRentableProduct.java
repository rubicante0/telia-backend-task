package com.example.backend.product.web.v1;

import com.example.backend.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ExternalRentableProduct {
    private final Long id;
    private final String title;

    public static ExternalRentableProduct from(Product product) {
        return ExternalRentableProduct.builder()
                .withId(product.getId())
                .withTitle(product.getTitle())
                .build();
    }
}
