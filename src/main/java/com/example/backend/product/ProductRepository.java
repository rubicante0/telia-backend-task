package com.example.backend.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRentable(boolean rentable);

    @EntityGraph(value = "product.prices")
    Optional<Product> findByIdAndRentable(Long id, boolean rentable);
}
