package com.fsc.service;

import com.fsc.model.form.NameListForm;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

public interface PodApiV1 {
    Boolean deletePodByPodNameList(String nameSpace, NameListForm podNameForm);

    Pod getPodByName(String nameSpace, String podName);

    PodList getPods();

    PodList getPodByDeploymentName(String namesapce, String deploymentName);
}
