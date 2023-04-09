package com.fsc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fsc.exception.SystemException;
import com.fsc.model.form.AppPort;
import com.fsc.model.form.NameListForm;
import com.fsc.model.vo.ServiceListVo;
import com.fsc.service.ServiceApiV1;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
@Slf4j
public class ServiceApiV1Impl implements ServiceApiV1 {
    @Autowired
    private KubernetesClient client;

    @Override
    public ServiceList getService() {
        return client.services().list();
    }

    @Override
    public Service addServiceWithApp(String nameSpace, String serviceName, String serviceType, Map<String, String> labelsMap, List<AppPort> portList) {
        List<ServicePort> servicePorts = new ArrayList<>();
        portList.stream().forEach(o -> {
            ServicePort servicePort = new ServicePort();
            servicePort.setName(o.getName());
            servicePort.setPort(o.getContainerPort());
            servicePort.setProtocol(o.getProtocol());
            servicePorts.add(servicePort);
        });
        Service service = new ServiceBuilder()
                .withNewMetadata()
                .withName(serviceName)
                .withNamespace(nameSpace)
                .endMetadata()
                .withNewSpec()
                .withSelector(labelsMap)
                .withPorts(servicePorts)
                .withType(serviceType)
                .endSpec()
                .build();

        Service serviceRes = client.services().resource(service).createOrReplace();
        return serviceRes;
    }

    @Override
    public ServiceList getServiceWithNameSpace(String nameSpace) {
        return client.services().inNamespace(nameSpace).list();
    }

    @Override
    public Service getServiceByName(String nameSpace, String name) {
        return client.services().inNamespace(nameSpace).withName(name).get();
    }

    @Override
    public Boolean addService(String yaml) {
        ServiceList service = Serialization.unmarshal(yaml, ServiceList.class);
        log.info("serviceList:{}", service);
        List<HasMetadata> hasMetadata = client.resourceList(yaml).create();
        return hasMetadata.size() > 0 ? true : false;
    }

    @Override
    public Boolean deleteService(String nameSpace, String serviceName) {
        List<StatusDetails> delete = client.services().inNamespace(nameSpace).withName(serviceName).delete();
        return delete.size() > 0 ? true : false;
    }

    @Override
    public Service updateService(String yaml) {
        try {
            Service service = Serialization.yamlMapper().readValue(yaml, Service.class);
            Service replace = client.services().resource(service).replace();
            return replace;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getServiceYaml(String nameSpace, String name) {
        Service service = client.services().inNamespace(nameSpace).withName(name).get();
        service.getMetadata().setManagedFields(null);
        return Serialization.asYaml(service);
    }

    @Override
    public Boolean deleteServiceList(String nameSpace, NameListForm serviceNameForm) {

        serviceNameForm.getNameList().stream().forEach(name -> {
            List<StatusDetails> delete = client.services().inNamespace(nameSpace).withName(name).delete();
            if (delete.size() == 0) {
                throw new SystemException("删除service错误");
            }
        });

        return true;
    }

    @Override
    public ServiceListVo getServiceTcpPortByName(String nameSpace, String name, String protocol) {
        Service service = client.services().inNamespace(nameSpace).withName(name).get();
        List<ServicePort> collect = service.getSpec().getPorts().stream().filter(o -> o.getProtocol().equals(protocol)).collect(Collectors.toList());
        ServiceListVo serviceListVo = new ServiceListVo();
        serviceListVo.setPortList(collect);
        serviceListVo.setTotal(collect.size());
        return serviceListVo;
    }

    @Override
    public Boolean updateTcpPortByName(String nameSpace, String name, List<ServicePort> portList) {
        Service service = client.services().inNamespace(nameSpace).withName(name).get();
        service.getSpec().setPorts(portList);
        Service orReplace = client.services().resource(service).replace();
        return orReplace != null;
    }

}
