package com.fsc.service.impl;

import com.fsc.model.vo.ConfigMapDataVo;
import com.fsc.model.vo.ConfigMapListVo;
import com.fsc.service.ConfigMapApiV1;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ConfigMapApiV1Impl implements ConfigMapApiV1 {

    @Autowired
    private KubernetesClient client;

    @Override
    public ConfigMap getConfigMapByName(String nameSpace, String name) {
        ConfigMap configMap = client.configMaps().inNamespace(nameSpace).withName(name).get();
        return configMap;
    }

    @Override
    public ConfigMapListVo getConfigMapList(String nameSpace) {
        ConfigMapList list = client.configMaps().inNamespace(nameSpace).list();
        ConfigMapListVo configMapListVo = new ConfigMapListVo();
        configMapListVo.setConfigMapList(list.getItems());
        configMapListVo.setTotal(list.getItems().size());
        return configMapListVo;
    }

    @Override
    public ConfigMapDataVo getConfigMapData(String nameSpace, String name) {
        ConfigMap configMap = client.configMaps().inNamespace(nameSpace).withName(name).get();
        Map<String, String> data = configMap.getData();
        ConfigMapDataVo configMapDataVo = new ConfigMapDataVo();
        configMapDataVo.setMap(data);
        return configMapDataVo;
    }

    @Override
    public Boolean updateConfigMapData(String nameSpace, String name, Map<String, String> map) {
        ConfigMap configMap = client.configMaps().inNamespace(nameSpace).withName(name).get();
        configMap.setData(map);
        return client.configMaps().resource(configMap).createOrReplace() != null;
    }


}
