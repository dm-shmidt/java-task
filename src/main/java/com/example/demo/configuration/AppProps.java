package com.example.demo.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "app.props")
@ConstructorBinding
public record AppProps(
        @NotNull
        @Min(200)
        BigDecimal balanceThreshold
) {
}
