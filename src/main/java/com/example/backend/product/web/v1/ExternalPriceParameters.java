package com.example.backend.product.web.v1;

import com.example.backend.product.PriceParameters;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
public class ExternalPriceParameters {
    @Min(1)
    private Integer commitmentInMonths;

    @Min(1)
    private Integer returnAfterMonths;

    @Pattern(regexp = "^[A-Z]{3}$")
    @NotNull
    private String currency = "EUR";

    public PriceParameters.PriceParametersBuilder toPriceParametersBuilder() {
        return PriceParameters.builder()
                .withCommitmentInMonths(commitmentInMonths)
                .withReturnAfterMonths(returnAfterMonths)
                .withCurrency(currency);
    }
}
