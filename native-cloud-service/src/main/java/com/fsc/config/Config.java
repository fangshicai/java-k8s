package com.fsc.config;

import com.fsc.feign.Harbor;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
public class Config {
    @Autowired
    private HarborConfigProperties harborConfigProperties;

    @Autowired
    private DockerConfigProperties dockerConfigProperties;

    // feign调harbor
    @Bean
    public Harbor harbor() {
        final String usernamePassword = dockerConfigProperties.getUsername() + ":" + dockerConfigProperties.getPassword();
        final String base64 = Base64.getUrlEncoder().encodeToString(usernamePassword.getBytes());

        return Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .requestInterceptor(requestTemplate -> {
                    log.info("Authorization:{}", "Basic " + base64);
                    requestTemplate.header("Authorization", "Basic " + base64);
                })
                .target(Harbor.class, harborConfigProperties.getHarborHost());
    }

    // docker连镜像
    @Bean
    public DockerClient dockerClient() {
        log.info("《===开始加载docker配置===》");
        // DockerClientConfig
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost(dockerConfigProperties.getHost())
                .withRegistryUsername(dockerConfigProperties.getUsername())
                .withRegistryPassword(dockerConfigProperties.getPassword())
                .withRegistryUrl(dockerConfigProperties.getRegistryHost())
                .build();
        log.info("docker配置类加载成功");
        // DockerHttpClient
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        // 加载docker-client
        log.info("DockerHttpClient创建成功");
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        log.info("DockerClient创建成功");
        log.info("《===加载docker配置成功===》");
        return dockerClient;
    }




}
