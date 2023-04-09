package com.fsc.service;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface DockerApiV1 {

    String uploadImage(String projectName, String imageName, String tag, MultipartFile file);

    String downloadImage(String name, String tag, HttpServletResponse response);

    Boolean uploadWithJar(String project, String name, String tag, MultipartFile dockerFile, MultipartFile jarFile);

    String downloadImageGz(String name, String tag, HttpServletResponse response);

    String uploadImageTarGz(String projectName, String imageName, String tag, MultipartFile file);
}
