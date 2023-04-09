package com.fsc.controller;

import com.fsc.model.form.NameListForm;
import com.fsc.model.R;
import com.fsc.service.PodApiV1;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workloads/pod")
@Slf4j
@Api("Pod")
public class PodApiV1Controller {


    @Autowired
    private PodApiV1 podApiV1;

    @ApiOperation(value = "获取全部pod")
    @GetMapping
    public R<PodList> getPods() {
        return R.ok(podApiV1.getPods());

//        return R.ok(list);
    }

    @ApiOperation(value = "通过pod名获取pod")
    @GetMapping("/{namespace}/{podName}")
    public R<Pod> getPodByPodName(@PathVariable("namespace") String nameSpace,
                                  @PathVariable("podName") String podName) {
        return R.ok(podApiV1.getPodByName(nameSpace, podName));
    }

    @ApiOperation(value = "删除pod")
    @DeleteMapping("/{namespace}")
    public R deletePod(@PathVariable("namespace") String nameSpace, @RequestBody NameListForm podNameForm) {
        Boolean flag = podApiV1.deletePodByPodNameList(nameSpace, podNameForm);
        return flag ? R.ok() : R.fail();
    }

    @ApiOperation(value = "获取deployment下的pod")
    @GetMapping("getPodByDeploymentName/{namesapce}/{deploymentName}")
    public R getPodByDeploymentName(@PathVariable("namesapce") String namesapce, @PathVariable("deploymentName") String deploymentName) {
        PodList list = podApiV1.getPodByDeploymentName(namesapce, deploymentName);
        return R.ok(list);
    }


}
