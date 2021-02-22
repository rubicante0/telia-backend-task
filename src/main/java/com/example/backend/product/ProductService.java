package com.example.backend.product;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.backend.product.ProductConstants.PRODUCT_CACHE_NAME;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    @Cacheable(value = PRODUCT_CACHE_NAME)
    public List<Product> findRentable() {
        return repository.findByRentable(true);
    }

    @Cacheable(value = PRODUCT_CACHE_NAME)
    public Optional<Product> getProductWithPrices(Long id) {
        return repository.findByIdAndRentable(id, true);
    }
}
