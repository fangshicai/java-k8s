package com.fsc.controller;

import com.fsc.model.form.NameListForm;
import com.fsc.model.R;
import com.fsc.service.DeploymentApiV1;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workloads/deployment")
@Api("Deployment")
public class DeploymentApiV1Controller {

    @Autowired
    private DeploymentApiV1 deploymentApiV1;

    @ApiOperation(value = "获取全部deployment")
    @GetMapping
    public R<DeploymentList> getDeployment() {
        return R.ok(deploymentApiV1.deploymentList());
    }

    @ApiOperation(value = "创建deployment（yaml）")
    @PostMapping
    public R<List<HasMetadata>> createDeploymentYaml(@RequestBody String yaml) {
        return R.ok(deploymentApiV1.createDeploymentOrUpdate(yaml));
    }

    @ApiOperation(value = "获取deployment的yaml形式")
    @GetMapping("/{nameSpace}/{deploymentName}")
    public R<Deployment> getDeploymentByName(@PathVariable("nameSpace") String nameSpace, @PathVariable("deploymentName") String deploymentName) {
        Deployment deployment = deploymentApiV1.getDeploymentByName(nameSpace, deploymentName);
        return R.ok(deployment);
    }

    @ApiOperation(value = "删除deployment")
    @DeleteMapping("/{nameSpace}/{deploymentName}")
    public R deleteDeployment(@PathVariable("nameSpace") String nameSpace,
                              @PathVariable("deploymentName") String deploymentName) {
        Boolean flag = deploymentApiV1.deleteDeploymentByName(nameSpace, deploymentName);
        return flag ? R.ok() : R.fail();
    }

    @ApiOperation(value = "修改deployment（yaml）")
    @PutMapping
    public R<Deployment> updateDeployment(@RequestBody String yaml) {
        return R.ok(deploymentApiV1.updateDeployment(yaml));
    }

    @ApiOperation(value = "获取deployment的yaml形式")
    @GetMapping("/{nameSpace}/{deploymentName}/yaml")
    public R<String> getDeploymentYamlByName(@PathVariable("nameSpace") String nameSpace, @PathVariable("deploymentName") String deploymentName) {
        String yaml = deploymentApiV1.getDeploymentYamlByName(nameSpace, deploymentName);
        return R.ok(yaml);
    }

    @ApiOperation(value = "扩缩容")
    @PostMapping("/scale/{namespace}/{deploymentName}/{scaleNum}")
    public R scalePodByDeployName(@PathVariable("namespace") String namespace,
                                  @PathVariable("deploymentName") String deploymentName,
                                  @PathVariable("scaleNum") Integer scaleNum) {
        return R.ok(deploymentApiV1.scalePodByDeployName(namespace, deploymentName, scaleNum));
    }

    @ApiOperation(value = "通过Deployment名集合删除")
    @DeleteMapping("/{nameSpace}")
    public R deleteDeploymentByNameList(@PathVariable("nameSpace") String nameSpace, @RequestBody NameListForm nameListForm) {
        return R.ok(deploymentApiV1.deleteDeploymentByNameList(nameSpace, nameListForm));
    }


}
