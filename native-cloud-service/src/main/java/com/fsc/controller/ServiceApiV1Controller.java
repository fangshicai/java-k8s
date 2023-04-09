package com.fsc.controller;

import com.fsc.model.form.NameListForm;
import com.fsc.model.R;
import com.fsc.model.vo.ServiceListVo;
import com.fsc.service.ServiceApiV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service")
@Api("service")
public class ServiceApiV1Controller {


    @Autowired
    private ServiceApiV1 serviceApiV1;

    @ApiOperation(value = "获取全部service")
    @GetMapping
    public R getService() {
        return R.ok(serviceApiV1.getService());
    }

    @ApiOperation(value = "获取nameSpace下的service")
    @GetMapping("/{nameSpace}")
    public R getServiceInNameSpace(@PathVariable("nameSpace") String nameSpace) {
        return R.ok(serviceApiV1.getServiceWithNameSpace(nameSpace));
    }

    @ApiOperation(value = "通过service名获取service")
    @GetMapping("/{nameSpace}/{name}")
    public R getServiceByName(@PathVariable("nameSpace") String nameSpace, @PathVariable("name") String name) {
        return R.ok(serviceApiV1.getServiceByName(nameSpace, name));
    }

    @ApiOperation(value = "获取Service的yaml格式")
    @GetMapping("/{nameSpace}/{name}/yaml")
    public R<String> serviceYaml(@PathVariable("nameSpace") String nameSpace, @PathVariable("name") String name) {
        return R.ok(serviceApiV1.getServiceYaml(nameSpace, name));
    }


    @ApiOperation(value = "新增service")
    @PostMapping
    public R addService(@RequestBody String yaml) {
        return R.ok(serviceApiV1.addService(yaml));
    }

    @ApiOperation(value = "删除service通过名字")
    @DeleteMapping("/{namespace}/{name}")
    public R deleteService(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        return R.ok(serviceApiV1.deleteService(namespace, name));
    }

    @ApiOperation(value = "删除service通过名字List集合")
    @DeleteMapping("/{namespace}")
    public R deleteService(@PathVariable("namespace") String namespace, @RequestBody NameListForm deploymentNameForm) {
        return R.ok(serviceApiV1.deleteServiceList(namespace, deploymentNameForm));
    }


    @ApiOperation(value = "更改service通过yaml")
    @PutMapping
    public R updateServiceYaml(@RequestBody String yaml) {
        return R.ok(serviceApiV1.updateService(yaml));
    }

    @ApiOperation(value = "获取service下的暴露的端口")
    @GetMapping("/namespace/{namespace}/name/{name}/tcpPort")
    public R<ServiceListVo> getServiceTcpPort(
            @ApiParam(value = "命名空间, 如：default") @PathVariable(value = "namespace") String nameSpace,
            @ApiParam(value = "service名") @PathVariable(value = "name") String name,
            @ApiParam(value = "Protocol类型, 如TCP或UDP") String protocol
    ) {
        ServiceListVo serviceListVo = serviceApiV1.getServiceTcpPortByName(nameSpace, name, protocol);
        return R.ok(serviceListVo);
    }
}
