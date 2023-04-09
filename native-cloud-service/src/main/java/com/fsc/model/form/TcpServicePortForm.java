package com.fsc.model.form;

import lombok.Data;

@Data
public class TcpServicePortForm {

    /**
     * service的命名空间
     */
    private String serviceNameSpace;
    /**
     * service名
     */
    private String serviceName;
    /**
     * service端口
     */
    private Integer servicePort;
    /**
     * service对应暴露端口
     */
    private Integer exposeServicePort;
}
