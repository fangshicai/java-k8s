package com.fsc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String tokenHeader;
    private String secret;
    private Long expiration;
    private String tokenHead;
}
