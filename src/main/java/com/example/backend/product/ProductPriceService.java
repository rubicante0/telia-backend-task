package com.example.backend.product;

import com.example.backend.currency.CurrencyConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPriceService {
    private final ProductService productService;
    private final CurrencyConverterService currencyConverterService;

    public Optional<BigDecimal> getProductPrice(PriceParameters priceParameters) {
        return productService.getProductWithPrices(priceParameters.getProductId())
                .map(product -> calculateProductPrice(product, priceParameters));
    }

    private BigDecimal calculateProductPrice(Product product, PriceParameters priceParameters) {
        int returnInMonths = getReturnInMonths(priceParameters);
        int defaultReturnInMonths = getDefaultReturnInMonths(priceParameters);

        BigDecimal initialCharge = getInitialCharge(product);
        BigDecimal monthlyPrice = priceParameters.getCommitmentInMonths() != null && returnInMonths == defaultReturnInMonths
                ? getMonthlyPrice(product, priceParameters.getCommitmentInMonths())
                : initialCharge;
        BigDecimal totalMonths = BigDecimal.valueOf(getReturnInMonths(priceParameters));

        BigDecimal resultInEuros = initialCharge.add(monthlyPrice.multiply(totalMonths));
        return currencyConverterService.convert(resultInEuros, priceParameters.getCurrency());
    }

    BigDecimal getInitialCharge(Product product) {
        return product.getPrices().stream()
                .filter(price -> price.getCommitmentInMonths() == null)
                .map(Price::getValue)
                .findFirst()
                .orElseThrow(() -> new PriceNotConfiguredException("Product " + product.getId() + " does not have no-commitment price configured"));
    }

    BigDecimal getMonthlyPrice(Product product, int commitmentInMonths) {
        return product.getPrices().stream()
                .filter(price -> Objects.equals(commitmentInMonths, price.getCommitmentInMonths()))
                .map(Price::getValue)
                .findFirst()
                .orElseThrow(() -> new PriceNotConfiguredException("Product " + product.getId() + " does not have price configured for " + commitmentInMonths + " months commitment"));
    }

    int getReturnInMonths(PriceParameters priceParameters) {
        if (priceParameters.getReturnAfterMonths() != null) {
            return priceParameters.getReturnAfterMonths();
        }
        return getDefaultReturnInMonths(priceParameters);
    }

    int getDefaultReturnInMonths(PriceParameters priceParameters) {
        if (priceParameters.getCommitmentInMonths() == null) {
            return 1;
        }
        return priceParameters.getCommitmentInMonths();
    }

    public static class PriceNotConfiguredException extends RuntimeException {
        public PriceNotConfiguredException(String message) {
            super(message);
        }
    }
}
