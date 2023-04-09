package com.fsc.model.k8s;

import lombok.Data;

import java.util.Map;

@Data
public class K8sNamespaceForm {
    private String name;
    private Map<String,String> labels;
}
