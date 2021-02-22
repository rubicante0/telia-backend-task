package com.example.backend.product.web.v1;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"db", "password"})
class ProductControllerTest {

    @LocalServerPort
    private int serverPort;

    @Test
    void shouldRequireBasicAuthentication() {
        RestTemplate template = createTemplate();

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> template.getForEntity("/api/v1/products", TestProduct[].class));
        assertThat(exception.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldFetchResultsWithAuthentication() {
        RestTemplate template = createTemplateWithAuthentication();
        TestProduct[] result = template.getForObject("/api/v1/products", TestProduct[].class);
        assertThat(result)
                .isNotEmpty();
    }

    private RestTemplate createTemplate() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:" + serverPort)
                .build();
    }

    private RestTemplate createTemplateWithAuthentication() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:" + serverPort)
                .basicAuthentication("test-login", "test-password")
                .build();
    }

    @Data
    private static class TestProduct {
        private Long id;
        private String title;
    }
}