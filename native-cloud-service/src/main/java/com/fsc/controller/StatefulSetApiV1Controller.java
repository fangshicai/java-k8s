package com.fsc.controller;

import com.fsc.model.form.NameListForm;
import com.fsc.model.R;
import com.fsc.service.StatefulSetApiV1;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workloads/statefulSet")
@Api("statefulSet")
public class StatefulSetApiV1Controller {

    @Autowired
    private KubernetesClient client;

    @Autowired
    private StatefulSetApiV1 statefulSetApiV1;

    @ApiOperation(value = "获取全部statefulSet")
    @GetMapping("/statefulSet")
    public R<StatefulSetList> getStatefulSet() {
        return R.ok(statefulSetApiV1.statefulSetList());
    }

    @ApiOperation(value = "获取nameSpace下statefulSet")
    @GetMapping("/{nameSpace}")
    public R<StatefulSetList> getStatefulSetList(@PathVariable("nameSpace") String nameSpace) {
        return R.ok(statefulSetApiV1.statefulSetListInNameSpace(nameSpace));
    }

    @ApiOperation(value = "通过statefulSet名获取")
    @GetMapping("/{namespace}/{name}")
    public R<StatefulSet> getStatefulSetByName(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return R.ok(statefulSetApiV1.statefulSetByName(namespace, name));
    }

    @ApiOperation(value = "通过statefulSet名获取")
    @GetMapping("/{namespace}/{name}/yaml")
    public R<String> getStatefulSetYamlByName(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return R.ok(statefulSetApiV1.statefulSetYamlByName(namespace, name));
    }

    @ApiOperation(value = "通过yaml新增statefulSet")
    @PostMapping
    public R addStatefulSetYaml(@RequestBody String yaml) {
        return R.ok(statefulSetApiV1.addStatefulSetYaml(yaml));
    }

    @ApiOperation(value = "通过yaml更新statefulSet")
    @PutMapping
    public R<StatefulSet> updateStatefulSetYaml(@RequestBody String yaml) {
        return R.ok(statefulSetApiV1.updateStatefulSet(yaml));
    }

    @ApiOperation(value = "通过statefulSet名删除")
    @DeleteMapping("/{namespace}/{name}")
    public R deleteStatefulSetByName(@PathVariable("namespace") String nameSpace, @PathVariable("name") String name) {
        return R.ok(statefulSetApiV1.deleteStatefulSetByName(nameSpace, name));
    }

    @ApiOperation(value = "通过statefulSet名List删除")
    @DeleteMapping("/{namespace}")
    public R deleteStatefulSetByName(@PathVariable("namespace") String nameSpace, @RequestBody NameListForm nameList) {
        return R.ok(statefulSetApiV1.deleteStatefulSetByNameList(nameSpace, nameList));
    }
}
