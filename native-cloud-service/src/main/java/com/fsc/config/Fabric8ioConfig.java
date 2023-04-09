package com.fsc.config;

import com.fsc.Application;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.utils.IOHelpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

@Configuration
@Slf4j
public class Fabric8ioConfig {
    @Autowired
    private K8sConfigProperties k8SConfigProperties;

    @Bean
    public KubernetesClient client() {
        log.info("《===开始加载k8s配置===》");
        // KubernetesClient
        // 获取admin.conf的内容的流
        final String conf = k8SConfigProperties.getConfigFile();
        log.info("k8s-conf:{}", conf);
        URL resource = Application.class.getClassLoader().getResource(conf);
//        URL resource = null;
        if (resource == null) {
            log.error("k8s config file 为 null: {}", conf);
            throw new RuntimeException("加载不到k8s配置类");
        }
        // 构建KubernetesClient
        try {
            // 获取admin-conf配置文件内容，通过IOHelpers把：流 ---> string
            String strConfContent = IOHelpers.readFully(resource.openStream());
            // 配置config
            Config config = Config.fromKubeconfig(strConfContent);
            // 构建client
            KubernetesClient clientBuild = new KubernetesClientBuilder().withConfig(config).build();
            if (clientBuild == null) {
                log.info("加载k8s配置文件错误！！！，k8s-client为空。");
                throw new RuntimeException("构建k8s的client错误！！！");
            }
            log.info("master-host:{}", clientBuild.getMasterUrl().getHost());
            log.info("连接master-port:{}", clientBuild.getMasterUrl().getPort());
            log.info("《===加载k8s配置成功===》");
            return clientBuild;

        } catch (IOException e) {
            log.error("输入流错误，请查看日志; 报错:{}", e);
            throw new RuntimeException(e);
        }
    }
}
