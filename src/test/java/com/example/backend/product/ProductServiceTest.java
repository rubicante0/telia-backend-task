package com.example.backend.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private Product product;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(repository);
    }

    @Test
    void findRentableShouldDelegateToRepository() {
        List<Product> expected = Collections.singletonList(product);
        when(repository.findByRentable(true))
                .thenReturn(expected);

        List<Product> result = service.findRentable();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getProductWithPricesShouldDelegateToRepository() {
        when(repository.findByIdAndRentable(100L, true))
                .thenReturn(Optional.of(product));

        Optional<Product> result = service.getProductWithPrices(100L);
        assertThat(result).hasValue(product);
    }
}
