package com.fsc.service;

import com.fsc.model.form.AppPort;
import com.fsc.model.form.NameListForm;
import com.fsc.model.vo.ServiceListVo;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;

import java.util.List;
import java.util.Map;

public interface ServiceApiV1 {
    ServiceList getService();

    Service addServiceWithApp(String nameSpace, String serviceName, String serviceType, Map<String, String> labelsMap, List<AppPort> list);

    ServiceList getServiceWithNameSpace(String nameSpace);

    Service getServiceByName(String nameSpace, String name);

    Boolean addService(String yaml);

    Boolean deleteService(String nameSpace, String serviceName);

    Service updateService(String yaml);

    String getServiceYaml(String nameSpace, String name);

    Boolean deleteServiceList(String nameSpace, NameListForm serviceNameForm);

    ServiceListVo getServiceTcpPortByName(String nameSpace, String name, String protocol);

    Boolean updateTcpPortByName(String nameSpace, String name, List<ServicePort> portList);
}
