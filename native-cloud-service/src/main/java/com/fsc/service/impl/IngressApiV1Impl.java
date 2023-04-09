package com.fsc.service.impl;

import com.fsc.exception.SystemException;
import com.fsc.model.form.IngressForm;
import com.fsc.model.form.TcpServicePortForm;
import com.fsc.service.ConfigMapApiV1;
import com.fsc.service.IngressApiV1;
import com.fsc.service.ServiceApiV1;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IngressApiV1Impl implements IngressApiV1 {

    @Autowired
    private KubernetesClient client;

    @Autowired
    private ConfigMapApiV1 configMapApiV1;

    @Autowired
    private ServiceApiV1 serviceApiV1;

    @Transient
    @Override
    public String exposeTcpService(TcpServicePortForm tcpServicePortForm) {
        // 判空

//        String configMapKey = tcpServicePortForm.
        // 设置ingress-nginx-controller
        String ingSVCNS = "ingress-nginx";
        String ingSVC = "ingress-nginx-controller";
        // 先修改ingress-nginx的service
        ServicePort servicePort = new ServicePort();
        servicePort.setProtocol("TCP");
        servicePort.setPort(tcpServicePortForm.getExposeServicePort());
        servicePort.setName(tcpServicePortForm.getServiceName());
        servicePort.setTargetPort(new IntOrString(tcpServicePortForm.getExposeServicePort()));
        servicePort.setNodePort(tcpServicePortForm.getExposeServicePort());
//        if ()
        List<ServicePort> tcpPortList = serviceApiV1.getServiceTcpPortByName(ingSVCNS, ingSVC, "TCP").getPortList();
        tcpPortList.add(servicePort);
        Boolean aBoolean = serviceApiV1.updateTcpPortByName(ingSVCNS, ingSVC, tcpPortList);
        if (!aBoolean) {
            throw new SystemException("创建服务错误");
        }

        String ingTcpCM = "tcp-services";
        // 修改ingress-nginx的tcp-services
        String configMapValue = tcpServicePortForm.getServiceNameSpace() + "/" + tcpServicePortForm.getServiceName() + ":" + tcpServicePortForm.getServicePort();
        Map<String, String> map = configMapApiV1.getConfigMapData(ingSVCNS, ingTcpCM).getMap();
        map.put(String.valueOf(tcpServicePortForm.getExposeServicePort()), configMapValue);
        Boolean aBoolean1 = configMapApiV1.updateConfigMapData(ingSVCNS, ingTcpCM, map);
        if (!aBoolean1) {
            throw new SystemException("创建服务错误");
        }
        return aBoolean1 ? "ok" : "false";
        //
    }

    @Override
    public String ingressBalance(IngressForm ingressForm) {
        // 判断service和端口是否存在

        // 判断pathType类型

        // path路径是否重复

//        client.network().ingress().inNamespace(ingressForm.getIngressNameSpace()).withName(ingressForm.getIngressName())
//                .s
        IngressRule ingressRule = new IngressRule();
        HTTPIngressRuleValue httpIngressRuleValue = new HTTPIngressRuleValue();
        List<HTTPIngressPath> httpIngressPaths = new ArrayList<>();
        HTTPIngressPath httpIngressPath = new HTTPIngressPath();
        httpIngressPath.setPath(ingressForm.getPath());
        httpIngressPath.setPathType(ingressForm.getPathType());
        IngressBackend ingressBackend = new IngressBackend();
        IngressServiceBackend ingressServiceBackend = new IngressServiceBackend();
        ingressServiceBackend.setName(ingressForm.getServiceName());
        ServiceBackendPort serviceBackendPort = new ServiceBackendPort();
        serviceBackendPort.setNumber(ingressForm.getServicePort());
        ingressServiceBackend.setPort(serviceBackendPort);
        ingressBackend.setService(ingressServiceBackend);
        httpIngressPath.setBackend(ingressBackend);
        httpIngressPaths.add(httpIngressPath);
        httpIngressRuleValue.setPaths(httpIngressPaths);
        ingressRule.setHttp(httpIngressRuleValue);

        Ingress build = new IngressBuilder()
                .withNewMetadata()
                .withName(ingressForm.getIngressName())
                .withNamespace(ingressForm.getIngressNameSpace())
                .endMetadata()
                .withNewSpec()
                .withRules(ingressRule)
                .withIngressClassName(ingressForm.getIngressClassName())
                .endSpec()
                .build();
        client.network().v1().ingresses().resource(build).createOrReplace();
        return "ok";
    }
}
