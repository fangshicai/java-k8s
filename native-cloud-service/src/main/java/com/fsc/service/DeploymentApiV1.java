package com.fsc.service;

import com.fsc.model.form.AppForm;
import com.fsc.model.form.NameListForm;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

import java.util.List;

public interface DeploymentApiV1 {
    public DeploymentList deploymentList();

    Boolean deleteDeploymentByName(String nameSpace, String deploymentName);

    List<HasMetadata> createDeploymentOrUpdate(String yaml);

    String getDeploymentYamlByName(String nameSpace, String deploymentName);

    Deployment updateDeployment(String yaml);

    String scalePodByDeployName(String namespace, String deploymentName, Integer scaleNum);

    Boolean deleteDeploymentByNameList(String nameSpace, NameListForm nameListForm);

    Deployment getDeploymentByName(String nameSpace, String deploymentName);

    Deployment addServiceWithApp(AppForm appForm);
}
