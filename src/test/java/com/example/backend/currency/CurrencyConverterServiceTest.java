package com.example.backend.currency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {
    private final static LocalDate FIXED_DATE = LocalDate.of(2021, 2, 11);

    @Mock
    private CurrencyConverterClient client;

    private CurrencyConverterService service;

    @BeforeEach
    void setUp() {
        Instant instant = FIXED_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Clock clock = Clock.fixed(instant, ZoneId.systemDefault());
        service = new CurrencyConverterService(client, clock);
    }

    @Test
    void convertShouldReturnPriceForEUR() {
        BigDecimal result = service.convert(BigDecimal.TEN, "EUR");
        assertThat(result)
                .isEqualTo(BigDecimal.TEN);

        verify(client, never())
                .getRate(any(), any());
    }

    @Test
    void convertShouldUseClientForOther() {
        when(client.getRate("SEK", FIXED_DATE))
                .thenReturn(BigDecimal.valueOf(15));

        BigDecimal result = service.convert(BigDecimal.TEN, "SEK");
        assertThat(result)
                .isEqualTo(BigDecimal.valueOf(150));
    }
}