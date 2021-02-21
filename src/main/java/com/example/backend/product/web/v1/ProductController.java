package com.example.backend.product.web.v1;

import com.example.backend.product.PriceParameters;
import com.example.backend.product.ProductPriceService;
import com.example.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    @GetMapping
    public List<ExternalProduct> findAllRentable() {
        return productService.findRentable().stream()
                .map(ExternalProduct::from)
                .collect(Collectors.toList());
    }

    @GetMapping("{productId}")
    public ExternalProductWithCommitments getDetails(@PathVariable("productId") Long productId) {
        return productService.getProductWithPrices(productId)
                .map(ExternalProductWithCommitments::from)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + productId));
    }

    @GetMapping("{productId}/price")
    public ExternalPrice getProductPrice(@PathVariable("productId") Long productId, @Valid @ModelAttribute ExternalPriceParameters parameters) {
        PriceParameters priceParameters = parameters.toPriceParametersBuilder()
                .withProductId(productId)
                .build();
        return productPriceService.getProductPrice(priceParameters)
                .map(price -> ExternalPrice.builder()
                        .withValue(price)
                        .build())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + productId));
    }

    @ExceptionHandler(ProductPriceService.PriceNotConfiguredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException() {
    }
}
