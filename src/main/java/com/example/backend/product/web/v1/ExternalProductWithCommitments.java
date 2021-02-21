package com.example.backend.product.web.v1;

import com.example.backend.product.Price;
import com.example.backend.product.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder(setterPrefix = "with")
public class ExternalProductWithCommitments {
    private final Long id;
    private final String title;
    private final List<Integer> commitments;

    public static ExternalProductWithCommitments from(Product product) {
        return ExternalProductWithCommitments.builder()
                .withId(product.getId())
                .withTitle(product.getTitle())
                .withCommitments(product.getPrices().stream()
                        .map(Price::getCommitmentInMonths)
                        .filter(Objects::nonNull)
                        .sorted()
                        .collect(Collectors.toList())
                )
                .build();
    }
}
