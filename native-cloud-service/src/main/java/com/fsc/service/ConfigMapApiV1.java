package com.fsc.service;

import com.fsc.model.vo.ConfigMapDataVo;
import com.fsc.model.vo.ConfigMapListVo;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;

import java.util.Map;

public interface ConfigMapApiV1 {

    ConfigMap getConfigMapByName(String nameSpace, String name);

    ConfigMapListVo getConfigMapList(String nameSpace);


    ConfigMapDataVo getConfigMapData(String nameSpace, String name);

    Boolean updateConfigMapData(String nameSpace, String name, Map<String, String> map);
}
