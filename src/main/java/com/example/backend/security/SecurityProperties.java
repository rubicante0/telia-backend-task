package com.example.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Data
@ConfigurationProperties("security.web")
public class SecurityProperties {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;
}
