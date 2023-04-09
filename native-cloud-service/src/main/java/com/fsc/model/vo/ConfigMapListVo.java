package com.fsc.model.vo;

import io.fabric8.kubernetes.api.model.ConfigMap;
import lombok.Data;

import java.util.List;

@Data
public class ConfigMapListVo {
    private List<ConfigMap> configMapList;
    private Integer total;
}
