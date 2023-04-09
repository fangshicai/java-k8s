package com.fsc.service;

import com.fsc.model.form.NameListForm;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;

public interface StatefulSetApiV1 {
    StatefulSetList statefulSetList();

    StatefulSetList statefulSetListInNameSpace(String nameSpace);

    StatefulSet statefulSetByName(String nameSpace, String name);

    Boolean addStatefulSetYaml(String yaml);

    Boolean deleteStatefulSetByName(String nameSpace, String name);

    StatefulSet updateStatefulSet(String yaml);

    Boolean deleteStatefulSetByNameList(String nameSpace, NameListForm nameList);

    String statefulSetYamlByName(String namespace, String name);
}
