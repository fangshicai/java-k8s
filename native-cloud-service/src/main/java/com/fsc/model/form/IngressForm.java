package com.fsc.model.form;

import lombok.Data;

@Data
public class IngressForm {
    /**
     * 负载均衡器名,nginx
     */
    private String ingressClassName;

    /**
     * ingress命名空间
     */
    private String ingressNameSpace;

    /**
     * ingress名
     */
    private String ingressName;
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 代理路径
     */
    private String path;

    /**
     * 路径类型,Exact或Prefix
     */
    private String pathType;


}
