package com.example.backend.product.web.v1;

import com.example.backend.product.Price;
import com.example.backend.product.Product;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder(setterPrefix = "with")
public class ExternalProductWithCommitments {
    @Schema(example = "1", description = "product identifier")
    private final Long id;

    @Schema(example = "Sony PS5", description = "product title")
    private final String title;

    @ArraySchema(schema = @Schema(example = "3"))
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
