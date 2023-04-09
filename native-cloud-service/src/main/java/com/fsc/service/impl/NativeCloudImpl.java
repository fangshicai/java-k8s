package com.fsc.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.fsc.service.NativeCloud;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NativeCloudImpl implements NativeCloud {
    @Autowired
    private KubernetesClient client;

    @Override
    public List<String> getNameSpace() {
        // 获取k8s命名空间名
        List<String> nameSpaceList = client.namespaces().list().getItems().stream().map(o -> o.getMetadata().getName()).collect(Collectors.toList());
//        PageUtil.getStart()
        List<String> list = ListUtil.page(0,2,nameSpaceList);
        list = null;
        boolean empty = CollectionUtils.isEmpty(list);
        log.info(String.valueOf(empty));
        return nameSpaceList;
    }

    @Override
    public List<DaemonSet> daemonSets() {
        DaemonSetList list = client.apps().daemonSets().list();
        return list.getItems();
    }
}
