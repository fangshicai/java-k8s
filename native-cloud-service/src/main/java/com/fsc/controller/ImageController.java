package com.fsc.controller;

import com.fsc.model.R;
import com.fsc.service.DockerApiV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/v1/image")
@Api(value = "镜像模块")
@Slf4j
public class ImageController {

    @Autowired
    private DockerApiV1 dockerApiV1;


    @PostMapping("/uploadImageTar")
    @ApiOperation(value = "上传镜像(tar)")
    public R uploadImage(@ApiParam(value = "项目名，如：test") @RequestParam String projectName,
                         @ApiParam(value = "镜像名，例如：nginx") @RequestParam String imageName,
                         @ApiParam(value = "tag，如：v1.0") @RequestParam String tag,
                         @ApiParam(value = "镜像格式为：xxx.tar") MultipartFile file) {
        String s = dockerApiV1.uploadImage(projectName, imageName, tag, file);
        return R.ok(s);
    }

    @GetMapping("/downloadImageTar")
    @ApiOperation(value = "下载镜像（tar）")
    public R<String> downloadImage(
            @ApiParam(value = "镜像名，可为test/nginx") @RequestParam String name,
            @ApiParam(value = "标签名：v1.0") @RequestParam String tag,
            HttpServletResponse response) {
        String flag = dockerApiV1.downloadImage(name, tag, response);
        return R.ok(flag);
    }

    @GetMapping("/downloadImageTarGz")
    @ApiOperation(value = "下载镜像压缩包（xxx.tar.gz）")
    public R<String> downloadImageWithGz(
            @ApiParam(value = "镜像名，可为test/nginx") @RequestParam String name,
            @ApiParam(value = "标签名：v1.0") @RequestParam String tag,
            HttpServletResponse response) {
        String flag = dockerApiV1.downloadImageGz(name, tag, response);
        return R.ok(flag);
    }

    @PostMapping("/uploadImageTarGz")
    @ApiOperation(value = "上传镜像压缩包（xxx.tar.gz）")
    public R<String> uploadImageWithTarGz(@ApiParam(value = "项目名") @RequestParam String projectName,
                                          @ApiParam(value = "镜像名，例如：192.168.200.131:30002/test/nginx") @RequestParam String imageName,
                                          @ApiParam(value = "tag，如：v1.0") @RequestParam String tag,
                                          @ApiParam(value = "镜像格式为：xxx.tar.gz") MultipartFile file) {
        String s = dockerApiV1.uploadImageTarGz(projectName, imageName, tag, file);
        return R.ok(s);
    }


    /**
     * 注意Dockerfile的Copy:COPY java-udp-test-0.0.1-SNAPSHOT.jar /app.jar
     *
     * @param project
     * @param name
     * @param tag
     * @param dockerFile
     * @param jarFile
     * @return
     * @throws IOException
     */
    @PostMapping("dockerBuild/{project}/{name}/{tag}")
    @ApiOperation(value = "构建镜像（DockerFile和jar）")
    public R<Boolean> dockerBuild(
            @ApiParam(value = "harbor的project名") @PathVariable("project") String project,
            @ApiParam(value = "镜像名") @PathVariable("name") String name,
            @ApiParam(value = "镜像标签") @PathVariable("tag") String tag,
            @RequestParam("DockerFile") MultipartFile dockerFile,
            @RequestParam("jar") MultipartFile jarFile) throws IOException {
        Boolean flag = dockerApiV1.uploadWithJar(project, name, tag, dockerFile, jarFile);

        return R.ok(flag);
    }


}
