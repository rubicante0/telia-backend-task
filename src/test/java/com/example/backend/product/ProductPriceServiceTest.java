package com.example.backend.product;

import com.example.backend.currency.CurrencyConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPriceServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private CurrencyConverterService currencyConverterService;

    @Mock
    private Product product;

    @Mock
    private PriceParameters parameters;

    private ProductPriceService service;

    @BeforeEach
    void setUp() {
        service = new ProductPriceService(productService, currencyConverterService);
    }

    @Test
    void shouldGetInitialCharge() {
        when(product.getPrices())
                .thenReturn(createPriceSet(100, 10, 20));

        BigDecimal result = service.getInitialCharge(product);
        assertThat(result).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldGetMonthlyPrice() {
        when(product.getPrices())
                .thenReturn(createPriceSet(11, 12, 13));

        BigDecimal result = service.getMonthlyPrice(product, 6);
        assertThat(result).isEqualTo(BigDecimal.valueOf(13));
    }

    @Test
    void getReturnInMonthsShouldUseProvidedValue() {
        when(parameters.getReturnAfterMonths())
                .thenReturn(11);

        int result = service.getReturnInMonths(parameters);
        assertThat(result).isEqualTo(11);
    }

    @Test
    void getReturnInMonthsShouldDefaultToOneFoNoCommitment() {
        when(parameters.getReturnAfterMonths())
                .thenReturn(null);
        when(parameters.getCommitmentInMonths())
                .thenReturn(null);

        int result = service.getReturnInMonths(parameters);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void getReturnInMonthsShouldDefaultToCommitment() {
        when(parameters.getReturnAfterMonths())
                .thenReturn(null);
        when(parameters.getCommitmentInMonths())
                .thenReturn(3);

        int result = service.getReturnInMonths(parameters);
        assertThat(result).isEqualTo(3);
    }

    @Test
    void shouldGetProductPriceWithCommitment() {
        when(product.getPrices())
                .thenReturn(createPriceSet(35, 30, 25));
        when(parameters.getCommitmentInMonths())
                .thenReturn(6);
        when(parameters.getReturnAfterMonths())
                .thenReturn(2);
        when(parameters.getProductId())
                .thenReturn(100L);
        when(parameters.getCurrency())
                .thenReturn("EUR");

        when(productService.getProductWithPrices(100L))
                .thenReturn(Optional.of(product));
        when(currencyConverterService.convert(any(), eq("EUR")))
                .thenAnswer(answer -> answer.getArgument(0));

        Optional<BigDecimal> result = service.getProductPrice(parameters);
        assertThat(result).hasValue(BigDecimal.valueOf(105));
    }

    @Test
    void shouldGetProductPriceWithoutCommitment() {
        when(product.getPrices())
                .thenReturn(createPriceSet(17, 13, 10));
        when(parameters.getCommitmentInMonths())
                .thenReturn(null);
        when(parameters.getReturnAfterMonths())
                .thenReturn(7);
        when(parameters.getProductId())
                .thenReturn(100L);
        when(parameters.getCurrency())
                .thenReturn("EUR");

        when(productService.getProductWithPrices(100L))
                .thenReturn(Optional.of(product));
        when(currencyConverterService.convert(any(), eq("EUR")))
                .thenAnswer(answer -> answer.getArgument(0));

        Optional<BigDecimal> result = service.getProductPrice(parameters);
        assertThat(result).hasValue(BigDecimal.valueOf(136));
    }

    private Set<Price> createPriceSet(int noCommitment, int threeMonths, int sixMonths) {
        return Set.of(
                Price.builder()
                        .withValue(BigDecimal.valueOf(noCommitment))
                        .build(),
                Price.builder()
                        .withCommitmentInMonths(3)
                        .withValue(BigDecimal.valueOf(threeMonths))
                        .build(),
                Price.builder()
                        .withCommitmentInMonths(6)
                        .withValue(BigDecimal.valueOf(sixMonths))
                        .build()
        );
    }
}