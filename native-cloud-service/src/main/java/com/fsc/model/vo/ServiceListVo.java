package com.fsc.model.vo;

import io.fabric8.kubernetes.api.model.ServicePort;
import lombok.Data;

import java.util.List;

@Data
public class ServiceListVo {
    private List<ServicePort> portList;
    private Integer total;
}
