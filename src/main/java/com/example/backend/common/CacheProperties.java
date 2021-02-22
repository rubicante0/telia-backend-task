package com.example.backend.common;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Validated
@Data
public class CacheProperties {
    private boolean enabled;

    @NotNull
    private Duration duration;
}
