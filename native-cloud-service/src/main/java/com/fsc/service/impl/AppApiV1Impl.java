package com.fsc.service.impl;

import com.fsc.model.form.AppForm;
import com.fsc.model.form.NameListForm;
import com.fsc.service.AppApiV1;
import com.fsc.service.DeploymentApiV1;
import com.fsc.service.ServiceApiV1;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AppApiV1Impl implements AppApiV1 {

    @Autowired
    private KubernetesClient client;
    @Autowired
    private ServiceApiV1 serviceApiV1;
    @Autowired
    private DeploymentApiV1 deploymentApiV1;

    @Override
    @Transactional
    public Object createApp(AppForm appForm) {
        // 设置label：name={name}
        Map<String, String> map = new HashMap<>();
        map.put("name", appForm.getDeploymentName());
        Deployment deployment = deploymentApiV1.addServiceWithApp(appForm);
        io.fabric8.kubernetes.api.model.Service service = serviceApiV1.addServiceWithApp(appForm.getNameSpace(), appForm.getServiceName(), appForm.getServiceType(), map, appForm.getFormList());
        return deployment;
    }

    @Override
    @Transactional
    public Object deleteApp(String nameSpace, NameListForm nameListForm) {
        nameListForm.getNameList().forEach(
                o -> {
                    serviceApiV1.deleteService(nameSpace, o);
                    deploymentApiV1.deleteDeploymentByName(nameSpace, o);
                }
        );
        return true;
    }


}
