package com.fsc.model.form;

import lombok.Data;

import java.util.List;

@Data
public class AppForm {
    /**
     * 命名空间，如：default
     */
    private String nameSpace;
    /**
     * deployment名
     */
    private String deploymentName;
    /**
     * service名
     */
    private String serviceName;
    /**
     * 镜像名，如：test/nginx:v1.0
     */
    private String image;
    /**
     * 服务类型，如：TCP/UDP
     */
    private String serviceType;
    /**
     * 端口集合
     */
    private List<AppPort> formList;
}
