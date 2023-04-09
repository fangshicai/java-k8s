package com.fsc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "docker")
@Data
@Component
public class DockerConfigProperties {
    private String host;
    private String username;
    private String password;
    private String registryHost;
}
