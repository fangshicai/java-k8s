package com.fsc.service;

import io.fabric8.kubernetes.api.model.apps.DaemonSet;

import java.util.List;

public interface NativeCloud {
    List<String> getNameSpace();

    List<DaemonSet> daemonSets();
}
