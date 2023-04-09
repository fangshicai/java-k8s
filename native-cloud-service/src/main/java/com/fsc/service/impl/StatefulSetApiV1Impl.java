package com.fsc.service.impl;

import com.fsc.model.form.NameListForm;
import com.fsc.service.StatefulSetApiV1;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatefulSetApiV1Impl implements StatefulSetApiV1 {

    @Autowired
    private KubernetesClient client;

    @Override
    public StatefulSetList statefulSetList() {
        return client.apps().statefulSets().list();
    }

    @Override
    public StatefulSetList statefulSetListInNameSpace(String nameSpace) {
        return client.apps().statefulSets().inNamespace(nameSpace).list();
    }

    @Override
    public StatefulSet statefulSetByName(String nameSpace, String name) {

        return client.apps().statefulSets().inNamespace(nameSpace).withName(name).get();
    }

    @Override
    public Boolean addStatefulSetYaml(String yaml) {
        StatefulSet statefulSet = Serialization.unmarshal(yaml, StatefulSet.class);
        StatefulSet resStatefulSet = client.apps().statefulSets().resource(statefulSet).create();
        return resStatefulSet != null ? true : false;
    }

    @Override
    public Boolean deleteStatefulSetByName(String nameSpace, String name) {
        List<StatusDetails> delete = client.apps().statefulSets().inNamespace(nameSpace).withName(name).delete();
        return delete != null ? true : false;
    }

    @Override
    public StatefulSet updateStatefulSet(String yaml) {
        StatefulSet statefulSet = Serialization.unmarshal(yaml, StatefulSet.class);
        StatefulSet replace = client.apps().statefulSets().resource(statefulSet).replace();
        return replace;
    }

    @Override
    public String statefulSetYamlByName(String namespace, String name) {
        StatefulSet statefulSet = client.apps().statefulSets().inNamespace(namespace).withName(name).get();
        statefulSet.getMetadata().setManagedFields(null);
        return Serialization.asYaml(statefulSet);
    }

    @Override
    public Boolean deleteStatefulSetByNameList(String nameSpace, NameListForm nameList) {
        nameList.getNameList().stream().forEach(name -> {
            client.apps().statefulSets().inNamespace(nameSpace).withName(name).delete();
        });
        return true;
    }
}
