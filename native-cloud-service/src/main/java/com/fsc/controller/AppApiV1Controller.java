package com.fsc.controller;

import com.fsc.model.form.AppForm;
import com.fsc.model.R;
import com.fsc.model.form.NameListForm;
import com.fsc.service.AppApiV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workloads/app")
@Api(value = "应用")
@Slf4j
public class AppApiV1Controller {

    @Autowired
    private AppApiV1 appApiV1;

    /**
     * 应用：deployment+service
     *
     * @param appForm
     * @return
     */
    @PostMapping
    @ApiOperation("创建应用deployment与service")
    public R createApp(@RequestBody AppForm appForm) {
        log.info(appForm.toString());
        Object app = appApiV1.createApp(appForm);
        return R.ok(app);
    }

    @ApiOperation("删除应用")
    @DeleteMapping("/{namespace}")
    public R deleteAppByName(@PathVariable("namespace") String nameSpace, @RequestBody NameListForm nameListForm) {
        return R.ok(appApiV1.deleteApp(nameSpace,nameListForm));
    }

}
