package com.example.backend.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("db")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindByRentable() {
        List<Product> result = productRepository.findByRentable(false);
        assertThat(result)
                .hasSize(1)
                .extracting(Product::getTitle)
                .containsExactly("Oculus Quest");
    }

    @Test
    void shouldFindByIdAndRentable() {
        Optional<Product> product = productRepository.findByIdAndRentable(5L, true);
        assertThat(product)
                .isNotEmpty()
                .get()
                .extracting(Product::getTitle)
                .isEqualTo("Oculus Quest 2");
        assertThat(product.get().getPrices())
                .hasSize(3)
                .extracting(Price::getCommitmentInMonths, Price::getValue)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, BigDecimal.valueOf(35)),
                        Tuple.tuple(3, BigDecimal.valueOf(30)),
                        Tuple.tuple(6, BigDecimal.valueOf(25)));
    }
}