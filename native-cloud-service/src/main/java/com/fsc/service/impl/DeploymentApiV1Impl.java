package com.fsc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsc.config.DockerConfigProperties;
import com.fsc.model.form.AppForm;
import com.fsc.model.form.NameListForm;
import com.fsc.service.DeploymentApiV1;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class DeploymentApiV1Impl implements DeploymentApiV1 {

    @Autowired
    private KubernetesClient client;

    @Autowired
    private DockerConfigProperties dockerConfigProperties;

    @Override
    public DeploymentList deploymentList() {
        KubernetesClient client = DeploymentApiV1Impl.this.client;
        DeploymentList list = client.apps().deployments().list();
        return list;
    }

    @Override
    public Boolean deleteDeploymentByName(String nameSpace, String deploymentName) {
        List<StatusDetails> delete = client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).delete();
        if (delete.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<HasMetadata> createDeploymentOrUpdate(String yaml) {
        List<HasMetadata> metadataList = client.resourceList(yaml).createOrReplace();
        return metadataList;
    }


    @Override
    public String getDeploymentYamlByName(String nameSpace, String deploymentName) {
        Deployment deployment = client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).get();
        deployment.getMetadata().setManagedFields(null);
        return Serialization.asYaml(deployment);
    }

    @Override
    public Deployment updateDeployment(String yaml) {
        ObjectMapper objectMapper = Serialization.yamlMapper();
        try {

            Deployment deployment = objectMapper.readValue(yaml, Deployment.class);

            Deployment replace = client.apps().deployments().resource(deployment).replace();
            return replace;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String scalePodByDeployName(String namespace, String deploymentName, Integer scaleNum) {
        KubernetesClient client = DeploymentApiV1Impl.this.client;
        // 先查deployment
        Deployment deployment = client.apps().deployments().inNamespace(namespace).withName(deploymentName).get();
        // 设置
        deployment.getSpec().setReplicas(scaleNum);
        // 更新
        Deployment replace = client.apps().deployments().resource(deployment).replace();
        log.info("deployment:{}", replace);
        return "ok";
    }

    @Override
    public Deployment getDeploymentByName(String nameSpace, String deploymentName) {
        return client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).get();
    }

    /**
     * 创建应用的deployment
     *
     * @param appForm
     * @return
     */
    @Override
    public Deployment addServiceWithApp(AppForm appForm) {

        // 设置label：name={name}
        Map<String, String> map = new HashMap<>();
        map.put("name", appForm.getDeploymentName());
        // 创建Container的端口
        Container container = new Container();

        // 遍历传入的port，port -> containerPort
        // appForm.getFormList()判空处理
        List<ContainerPort> ports = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appForm.getFormList())) {
            appForm.getFormList().forEach(o -> {
                ContainerPort containerPort = new ContainerPort();
                // 设置port名
                containerPort.setName(o.getName());
                // 设置port容器
                containerPort.setContainerPort(o.getContainerPort());
                // 设置port协议
                containerPort.setProtocol(o.getProtocol());
                // 添加到containerPort数组
                ports.add(containerPort);
            });
        }

        String imageRepo = dockerConfigProperties.getRegistryHost()+"/"+appForm.getImage();

        // 容器设置port
        container.setPorts(ports);
        // 设置镜像
        container.setImage(imageRepo);
        // 设置容器名，当前容器名为deployment名
        container.setName(appForm.getDeploymentName());
        // 构建器构建deployment
        Deployment build = new DeploymentBuilder()
                .withNewMetadata()
                // 设置命名空间
                .withNamespace(appForm.getNameSpace())
                // 设置deployment名
                .withName(appForm.getDeploymentName())
                .endMetadata()
                .withNewSpec()
                .withNewSelector()
                // 设置deployment标签
                .withMatchLabels(map)
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                //
                .withName(appForm.getDeploymentName())
                // 设置Template标签
                .withLabels(map)
                .endMetadata()
                .withNewSpec()
                // 设置容器
                .withContainers(container)
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
        Deployment deployment = client.apps().deployments().resource(build).createOrReplace();
        return deployment;
    }

    @Override
    public Boolean deleteDeploymentByNameList(String nameSpace, NameListForm nameListForm) {
        nameListForm.getNameList().stream().forEach(name -> {
            client.apps().deployments().inNamespace(nameSpace).withName(name).delete();
        });
        return true;
    }
}
