package com.example.backend.product.web.v1;

import com.example.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ExternalRentableProduct> findRentable() {
        return service.findRentable().stream()
                .map(ExternalRentableProduct::from)
                .collect(Collectors.toList());
    }
}
