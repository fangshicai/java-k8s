package com.fsc.service.impl;

import com.fsc.model.form.NameListForm;
import com.fsc.service.PodApiV1;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PodApiV1Impl implements PodApiV1 {
    @Autowired
    private KubernetesClient client;

    @Override
    public Boolean deletePodByPodNameList(String nameSpace, NameListForm podNameForm) {
        List<String> nameList = podNameForm.getNameList();
        nameList.stream().forEach(podName -> client.pods().inNamespace(nameSpace).withName(podName).delete());
        return true;
    }

    @Override
    public PodList getPodByDeploymentName(String namesapce, String deploymentName) {
        // 获取deployment与pod关联的标签
        Map<String, String> labels = client.apps().deployments().inNamespace(namesapce).withName(deploymentName).get().getSpec().getTemplate().getMetadata().getLabels();
        return client.pods().inNamespace(namesapce).withLabels(labels).list();
    }

    @Override
    public Pod getPodByName(String nameSpace, String podName) {
        Pod pod = client.pods().inNamespace(nameSpace).withName(podName).get();
        return pod;
    }

    @Override
    public PodList getPods() {
        return client.pods().list();
    }
}
