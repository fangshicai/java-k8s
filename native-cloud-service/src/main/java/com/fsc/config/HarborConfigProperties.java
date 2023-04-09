package com.fsc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "harbor")
public class HarborConfigProperties {
//    private String username;
//    private String password;
    private String harborHost;
    private String harborIp;
}
