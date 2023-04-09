package com.fsc.controller;

import com.fsc.exception.SystemException;
import com.fsc.model.R;
import com.fsc.model.k8s.K8sNamespaceForm;
import com.fsc.service.NativeCloud;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@ApiOperation("k8s基础管理")
public class NativeApiV1Controller {
    @Autowired
    private KubernetesClient client;

    @Autowired
    private NativeCloud nativeCloud;

    @GetMapping("/hello")
    public String sayHello() {
        return "hello world!!!";
    }

    @ApiOperation(value = "创建资源通过yaml")
    @PostMapping(path = "/createByYaml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R createDeploymentYaml(@RequestPart("file") MultipartFile multipart) {
        String name1 = multipart.getName();
//        String name = client.resourceList("apiVersion: v1\n" +
//                        "kind: ConfigMap\n" +
//                        "metadata:\n" +
//                        "  name: game-config-env-file\n" +
//                        "  namespace: default\n" +
//                        "data:\n" +
//                        "  allowed: '\"true\"'\n" +
//                        "  enemies: aliens\n" +
//                        "  lives: \"3\"")
//                .inNamespace("default").createOrReplace().get(0).getMetadata().getName();
        try {
            client.load(multipart.getInputStream()).inNamespace("default").createOrReplace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok("df");
    }

    @ApiOperation(value = "创建资源通过yaml")
    @PostMapping(path = "/createByYamlStr")
    public R createByYamlStr(@RequestBody String yamlStr) {
        log.info("yaml:{}", yamlStr);

        List<HasMetadata> metadataList = client.resourceList(yamlStr).createOrReplace();
        if (yamlStr.length() == 0) {
            throw new SystemException("错误");
        }
        return R.ok(metadataList);
    }


    @ApiOperation(value = "获取全部daemonSets")
    @GetMapping("/daemonSets")
    public R<List<DaemonSet>> daemonSets() {
        return R.ok(nativeCloud.daemonSets());
    }

    @ApiOperation(value = "获取全部命名空间")
    @GetMapping("/namespace")
    public R<List<String>> getNameSpace() {
        return R.ok(nativeCloud.getNameSpace());
    }


    @ApiOperation(value = "创建命名空间")
    @PostMapping("/namespace")
    public R createNameSpace(@RequestBody K8sNamespaceForm k8sNamespaceForm) {
        Namespace testNamespace = client.namespaces().resource(new NamespaceBuilder().withNewMetadata().withName(k8sNamespaceForm.getName()).withLabels(k8sNamespaceForm.getLabels()).endMetadata().build()).createOrReplace();
        return R.ok(testNamespace);
    }

    @ApiOperation("删除命名空间")
    @DeleteMapping("/namespace/{name}")
    public R deleteNamespace(@PathVariable("name") String name) {
        client.namespaces().withName(name).delete();
        return R.ok();
    }


//    @ApiOperation("创建名字空间（yaml）")
//    @PostMapping("/namespace/yaml")
//    public R createNamespaceYaml(@RequestBody String yaml){
//        KubernetesClient client = client;
//        client.load(CreateOrReplaceResourceList.class.getResourceAsStream())
//    }


}
