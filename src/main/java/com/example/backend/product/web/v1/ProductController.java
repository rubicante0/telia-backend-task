package com.example.backend.product.web.v1;

import com.example.backend.product.PriceParameters;
import com.example.backend.product.ProductPriceService;
import com.example.backend.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Slf4j
@SecurityRequirement(name = "basicAuth")
@Tag(name = "products", description = "products API")
public class ProductController {
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    @Operation(summary = "Gets products that are rentable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExternalProduct.class))))
    })
    @GetMapping
    public List<ExternalProduct> findAllRentable() {
        return productService.findRentable().stream()
                .map(ExternalProduct::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Gets rentable product details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ExternalProductWithCommitments.class))),
            @ApiResponse(responseCode = "404", description = "Rentable product not found", content = @Content)})
    @GetMapping("{productId}")
    public ExternalProductWithCommitments getDetails(
            @Parameter(example = "5", description = "Rentable product identifier", required = true)
            @PathVariable("productId") Long productId) {
        return productService.getProductWithPrices(productId)
                .map(ExternalProductWithCommitments::from)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + productId));
    }

    @Operation(summary = "Gets rentable product price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ExternalPrice.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect price parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rentable product not found", content = @Content)})
    @GetMapping("{productId}/price")
    public ExternalPrice getProductPrice(
            @Parameter(example = "5", description = "Rentable product identifier", required = true)
            @PathVariable("productId") Long productId,

            @Schema(example = "6", description = "commitment in months")
            @Min(1)
            @RequestParam(value = "commitmentInMonths", required = false) Integer commitmentInMonths,

            @Schema(example = "1", description = "return product after months")
            @Min(1)
            @RequestParam(value = "returnAfterMonths", required = false) Integer returnAfterMonths,

            @Schema(example = "SEK", description = "requested price currency", defaultValue = "EUR")
            @Pattern(regexp = "^[A-Z]{3}$")
            @NotNull
            @RequestParam(value = "currency", defaultValue = "EUR") String currency) {
        PriceParameters priceParameters = PriceParameters.builder()
                .withCommitmentInMonths(commitmentInMonths)
                .withReturnAfterMonths(returnAfterMonths)
                .withCurrency(currency)
                .withProductId(productId)
                .build();
        return productPriceService.getProductPrice(priceParameters)
                .map(price -> ExternalPrice.builder()
                        .withValue(price)
                        .build())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + productId));
    }

    @ExceptionHandler({ProductPriceService.PriceNotConfiguredException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(Exception exception) {
        log.error("Error processing request", exception);
    }
}
