package com.example.backend.currency;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("currency.web-service")
@Data
public class CurrencyConverterEndpointProperties {
    @NotEmpty
    private String endpoint;

    @Min(0)
    private int connectTimeout = 3000;

    @Min(0)
    private int readTimeout = 3000;
}
