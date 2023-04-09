package com.fsc.controller;

import com.fsc.model.R;
import com.fsc.model.vo.ConfigMapDataVo;
import com.fsc.model.vo.ConfigMapListVo;
import com.fsc.service.ConfigMapApiV1;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/storage/configMap")
@Api(value = "应用")
@Slf4j
public class ConfigMapV1Controller {

    @Autowired
    private ConfigMapApiV1 configMapApiV1;

    @GetMapping("/namespace/{namespace}")
    @ApiOperation(value = "获取命名空间下的configMap集合")
    public R<ConfigMapListVo> getConfigMapList(@ApiParam(value = "命名空间，如：default") @PathVariable(value = "namespace") String nameSpace) {

        ConfigMapListVo configMapList = configMapApiV1.getConfigMapList(nameSpace);
        return R.ok(configMapList);
    }

    @ApiOperation(value = "根据命名空间和名字获取configMap")
    @GetMapping("/namespace/{namespace}/name/{name}")
    public R<ConfigMap> getConfigMapByName(@ApiParam(value = "命名空间，如：default") @PathVariable(value = "namespace") String nameSpace,
                                           @ApiParam(value = "configmap名") @PathVariable(value = "name") String name
    ) {
        return R.ok(configMapApiV1.getConfigMapByName(nameSpace, name));
    }

    @ApiOperation(value = "获取configMap里的数据")
    @GetMapping("/namespace/{namespace}/name/{name}/data")
    public R<ConfigMapDataVo> getConfigMapData(@ApiParam(value = "命名空间，如：default") @PathVariable(value = "namespace") String nameSpace,
                                               @ApiParam(value = "configmap名") @PathVariable(value = "name") String name
    ) {
        return R.ok(configMapApiV1.getConfigMapData(nameSpace, name));
    }

    @ApiOperation(value = "更新configMap里的数据")
    @PutMapping("/namespace/{namespace}/name/{name}/data")
    public R<Boolean> updateConfigMapData(
            @ApiParam(value = "命名空间，如：default") @PathVariable(value = "namespace") String nameSpace,
            @ApiParam(value = "configmap名") @PathVariable(value = "name") String name,
            @ApiParam(value = "更新的数据，map形式") @RequestBody Map<String, String> map
            ) {
        return R.ok(configMapApiV1.updateConfigMapData(nameSpace, name,map));
    }


}
