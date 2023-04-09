package com.fsc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "k8s.api")
@Component
@Data
public class K8sConfigProperties {

    private String configFile;

}
