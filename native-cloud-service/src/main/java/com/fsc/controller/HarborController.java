package com.fsc.controller;

import com.fsc.model.R;
import com.fsc.model.form.HarborCreateForm;
import com.fsc.model.request.query.HarborProjectQuery;
import com.fsc.model.response.harbor.projects.ProjectList;
import com.fsc.model.response.harbor.repositories.RepositoryList;
import com.fsc.model.response.harbor.tags.TagList;
import com.fsc.model.response.images.ImageList;
import com.fsc.service.HarborApiV1;
import feign.QueryMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/harbor")
@Api(value = "镜像模块")
@Slf4j
public class HarborController {
    @Autowired
    private HarborApiV1 harborApiV1;

    @GetMapping("/projects")
    @ApiOperation(value = "获取harbor项目列表")
    public R<ProjectList> getProjects(@QueryMap HarborProjectQuery query) {
        log.info("");
        return R.ok(harborApiV1.projects(query));
    }

    @GetMapping("projects/{projectName}/repositories")
    @ApiOperation(value = "获取harbor仓库列表")
    public R<RepositoryList> getRepositories(
            @ApiParam(value = "项目名") @PathVariable("projectName") String projectName,
            @ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
            @ApiParam(value = "页数大小") @RequestParam(value = "page_size", required = false) Integer pageSize
    ) {
        return R.ok(harborApiV1.repositories(projectName, page, pageSize));
    }

    @GetMapping("projects/{projectName}/repositories/{repositoryName}/artifacts")
    @ApiOperation(value = "获取镜像tags")
    public R<TagList> getTags(
            @ApiParam(value = "项目名") @PathVariable("projectName") String projectName,
            @ApiParam(value = "镜像名") @PathVariable("repositoryName") String repositoryName,
            @ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
            @ApiParam(value = "页数大小") @RequestParam(value = "page_size", required = false) Integer pageSize
    ) {
        return R.ok(harborApiV1.tags(projectName, repositoryName, page, pageSize));
    }

    @GetMapping("projects/{projectName}/repositories/{repositoryName}/artifacts/images")
    @ApiOperation(value = "获取镜像tags")
    public R<ImageList> getImages(@ApiParam(value = "项目名") @PathVariable("projectName") String projectName,
                                  @ApiParam(value = "镜像名") @PathVariable("repositoryName") String repositoryName,
                                  @ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                  @ApiParam(value = "页数大小") @RequestParam(value = "page_size", required = false) Integer pageSize) {
        return R.ok(harborApiV1.images(projectName, repositoryName, page, pageSize));
    }

    @PostMapping("/project")
    @ApiOperation(value = "创建harbor仓库镜像项目")
    public R<String> createProject(@RequestBody HarborCreateForm harborCreateForm) {
        log.info(harborCreateForm.toString());
        return R.ok(harborApiV1.createProject(harborCreateForm));
    }

    @DeleteMapping("/project/{projectId}")
    @ApiOperation(value = "删除镜像仓库项目")
    public R<Boolean> deleteProject(@ApiParam(value = "仓库项目id") @PathVariable String projectId) {
        return R.ok(harborApiV1.deleteProjectById(projectId));
    }


}
