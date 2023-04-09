package com.fsc.service.impl;

import com.fsc.config.DockerConfigProperties;
import com.fsc.async.FuturePullImageResultCallback;
import com.fsc.exception.SystemException;
import com.fsc.model.docker.Manifest;
import com.fsc.service.DockerApiV1;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.concurrent.FutureCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@Slf4j
public class DockerApiV1Impl implements DockerApiV1 {

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private DockerConfigProperties dockerConfigProperties;

    // 自定义线程
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 获取镜像名，如：192.168.200.131:30002/test/nginx
     *
     * @param projectName
     * @param imageName
     * @return
     */
    public String getImageRepoName(String projectName, String imageName) {
        return dockerConfigProperties.getRegistryHost() + "/" + projectName + "/" + imageName;
    }

    /**
     * 上传镜像
     *
     * @param projectName
     * @param imageName
     * @param tag
     * @param file
     * @return
     */
    @Override
    public String uploadImage(String projectName, String imageName, String tag, MultipartFile file) {
        try {
            log.info("{} ，上传到harbor仓库开始", file.getOriginalFilename());
            // 获取镜像名如：192.168.200.131:30002/test/nginx
            String imageRepoName = this.getImageRepoName(projectName, imageName);
            // 从镜像tar包中获取镜像repoTag
            String imageTag = getImageTag(file.getInputStream());
            log.info("tar包下的镜像Tag为:{}", imageTag);
            // 加载镜像，然后暂时存在服务器docker本地
            log.info("镜像传到服务器docker---开始");
            dockerClient.loadImageCmd(file.getInputStream()).exec();
            log.info("镜像传到服务器docker---结束");
            // 打tag镜像
            dockerClient.tagImageCmd(imageTag, imageRepoName, tag).exec();
            // 推送到harbor仓库
            String imageTagPush = imageRepoName + ":" + tag;
            log.info("tag:{}--->{}", imageTag, imageTagPush);
            dockerClient.pushImageCmd(imageTagPush).exec(new ResultCallback<PushResponseItem>() {
                @Override
                public void onStart(Closeable closeable) {
                    log.info("开始上传镜像到harbor仓库");
                }

                @Override
                public void onNext(PushResponseItem object) {

                }

                @Override
                public void onError(Throwable throwable) {
                    log.info(String.valueOf(throwable.getCause()));
                }

                @Override
                public void onComplete() {
                    log.info("push镜像到harbor仓库成功");
                    dockerClient.removeImageCmd(imageTagPush).exec();
                    log.info("删除docker镜像：{} 成功", imageTagPush);
                    // 以下这个应该放在finally中,删掉
                    dockerClient.removeImageCmd(imageTag).exec();
                    log.info("删除镜像：{} 成功", imageTag);
                }

                @Override
                public void close() throws IOException {

                }
            });
            log.info("{} ，上传到harbor仓库结束", file.getOriginalFilename());
            return "ok";
        } catch (IOException e) {
            log.info("上传镜像错误：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传镜像（以gz压缩包的形式）
     * todo 待改进，目前是先把xxx.tar.gz解压为xxx.tar然后存放到服务器目录
     *
     * @param projectName
     * @param imageName
     * @param tag
     * @param file
     * @return
     */
    @Override
    public String uploadImageTarGz(String projectName, String imageName, String tag, MultipartFile file) {
        try {
            log.info("{} ，上传到harbor仓库开始", file.getOriginalFilename());
            // 获取镜像名如：192.168.200.131:30002/test/nginx
            String imageRepoName = this.getImageRepoName(projectName, imageName);
            // tar.gz ---> tar
            // 临时存放tar包的目录,在resources/tar/(UUID)/xxx.tar
            File fileParent = new File("api-native-cloud/native-cloud-service/src/main/resources/tar/" + UUID.randomUUID());
            // 创建目录
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            // 创建tar的file类型
            File fileTar = new File(fileParent.getAbsolutePath() + "/" + file.getOriginalFilename().replace(".gz", ""));
            OutputStream out = Files.newOutputStream(fileTar.toPath());

            InputStream fin = file.getInputStream();
            BufferedInputStream in = new BufferedInputStream(fin);
            GzipCompressorInputStream gzIn = new GzipCompressorInputStream(fin);
            int buffersize = 4096;
            final byte[] buffer = new byte[buffersize];
            int n = 0;
            while (-1 != (n = gzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            gzIn.close();
            out.close();
            in.close();
            fin.close();

            //从tar中获取镜像tag
            String imageTag = getImageTag(Files.newInputStream(fileTar.toPath()));
            // 删除临时tar
            FileUtils.deleteDirectory(fileParent);
            log.info("tar包下的镜像Tag为:{}", imageTag);
            // 加载镜像，然后暂时存在服务器docker本地
            log.info("镜像传到服务器docker---开始");
            dockerClient.loadImageCmd(file.getInputStream()).exec();
            log.info("镜像传到服务器docker---结束");
            // 打tag镜像
            dockerClient.tagImageCmd(imageTag, imageRepoName, tag).exec();
            // 推送到harbor仓库
            String imageTagPush = imageRepoName + ":" + tag;
            log.info("tag:{}--->{}", imageTag, imageTagPush);
            dockerClient.pushImageCmd(imageTagPush).exec(new ResultCallback<PushResponseItem>() {
                @Override
                public void onStart(Closeable closeable) {
                    log.info("开始上传镜像到harbor仓库");
                }

                @Override
                public void onNext(PushResponseItem object) {

                }

                @Override
                public void onError(Throwable throwable) {
                    log.info("镜像上传错误：{}", throwable.getCause());
                }

                @Override
                public void onComplete() {
                    log.info("push镜像到harbor仓库成功");
                    dockerClient.removeImageCmd(imageTagPush).exec();
                    log.info("删除docker镜像：{} 成功", imageTagPush);
                    // 以下这个应该放在finally中,删掉
                    dockerClient.removeImageCmd(imageTag).exec();
                    log.info("删除镜像：{} 成功", imageTag);
                }

                @Override
                public void close() throws IOException {

                }
            });
            log.info("{} ，上传到harbor仓库结束", file.getOriginalFilename());
            return "ok";
        } catch (IOException e) {
            log.info("上传镜像错误：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 下载镜像
     *
     * @param name
     * @param tag
     * @return
     */
    @Override
    public String downloadImage(String name, String tag, HttpServletResponse response) {

        // 名字：test/nginx:v1.0
        String repositoryName = name + ":" + tag;
        log.info("传入的镜像形式:{}", repositoryName);

        // 从harbor里拉取的镜像名：192.168.200.131:30002/test/nginx
        String harborRepoName = dockerConfigProperties.getRegistryHost() + "/" + name;

        // 从harbor里拉取的镜像名带tag:192.168.200.131:30002/test/nginx:v1.0
        String harborRepoTag = harborRepoName + ":" + tag;
        log.info("harbor-tag:{}", harborRepoTag);

        // 先拉取镜像
//        ExecutorService executorService = Executors.newCachedThreadPool();

        //异步回调
        FutureCallback<PullResponseItem> futureCallback = new FutureCallback<PullResponseItem>() {
            @Override
            public void completed(PullResponseItem pullResponseItem) {
                log.info("{}", pullResponseItem);
                log.info("拉取（pull）镜像：完成");

                // 打成harbor形式的镜像的tag
                log.info("下载镜像（tar）");
                log.info("镜像：{}", harborRepoTag);
                try (ServletOutputStream outputStream = response.getOutputStream();
                     // 保存镜像 .tar
                     InputStream inputStream = dockerClient.saveImageCmd(harborRepoTag).exec();
                ) {
                    response.setContentType("application/octet-stream");
                    final String filename = String.join("-", name, tag) + ".tar";
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + "filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8.displayName()));
                    //TODO 后续考虑优化下载压缩镜像
                    int buffersize = 4096;
                    final byte[] buffer = new byte[buffersize];
                    int n = 0;
                    while (-1 != (n = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, n);
                    }
                    outputStream.flush();
                    outputStream.close();
                    log.info("下载结束");
                    // 移除镜像
                    dockerClient.removeImageCmd(harborRepoTag).exec();
                    log.info("删除docker镜像：{} 成功", harborRepoTag);
                } catch (IOException e) {
                    log.info("下载失败");
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }

        };

        // docker pull
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(harborRepoTag);
        // threadPoolTaskExecutor为自定义线程
        Future<PullResponseItem> future = threadPoolTaskExecutor.submit(() -> {
            log.info("threadDocker:{}", Thread.currentThread().getName());
            FuturePullImageResultCallback futureCallbackWrapper = new FuturePullImageResultCallback();
            pullImageCmd.exec(futureCallbackWrapper);
            return futureCallbackWrapper.awaitResult();
        });

        try {
            PullResponseItem res = future.get(); // 等待异步操作完成
            futureCallback.completed(res);
        } catch (InterruptedException | ExecutionException e) {
            futureCallback.failed(e);
        }

        return "ok";
    }


    /**
     * 下载镜像以压缩包的形式（gz）
     *
     * @param name
     * @param tag
     * @param response
     * @return
     */
    @Override
    public String downloadImageGz(String name, String tag, HttpServletResponse response) {

        CountDownLatch latch = new CountDownLatch(1);
        // 名字：test/nginx:v1.0
        String repositoryName = name + ":" + tag;
        log.info("传入的镜像形式:{}", repositoryName);

        // 从harbor里拉取的镜像名：192.168.200.131:30002/test/nginx
        String harborRepoName = dockerConfigProperties.getRegistryHost() + "/" + name;

        // 从harbor里拉取的镜像名带tag:192.168.200.131:30002/test/nginx:v1.0
        String harborRepoTag = harborRepoName + ":" + tag;
        log.info("harbor-tag:{}", harborRepoTag);

        dockerClient.pullImageCmd(harborRepoTag).exec(new ResultCallback<PullResponseItem>() {
            @Override
            public void onStart(Closeable closeable) {
            }

            @Override
            public void onNext(PullResponseItem object) {

            }

            @Override
            public void onError(Throwable throwable) {

                log.error("onError:{}", throwable.getMessage());
                latch.countDown();
            }

            @Override
            public void onComplete() {
                log.info("拉取（pull）镜像：完成");

                // 打成harbor形式的镜像的tag
                log.info("下载镜像（tar）");
                log.info("镜像：{}", harborRepoTag);
                try (// 保存镜像 .tar.gz
                     InputStream inputStream = dockerClient.saveImageCmd(harborRepoTag).exec();
                ) {
                    final String filename = String.join("-", name, tag) + ".tar.gz";
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + "filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8.displayName()));
                    response.setContentType("application/octet-stream");
                    GzipCompressorOutputStream gzipCompressorOutputStream = new GzipCompressorOutputStream(response.getOutputStream());

                    int size = 4096;
                    byte[] buffer = new byte[size];
                    int n = 0;
                    while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        gzipCompressorOutputStream.write(buffer, 0, n);
                    }
                    gzipCompressorOutputStream.close();
                    inputStream.close();
                    log.info("下载结束");
                    // 移除镜像
                    dockerClient.removeImageCmd(harborRepoTag).exec();
                    log.info("删除docker镜像：{} 成功", harborRepoTag);
                } catch (IOException e) {
                    log.info("下载失败");
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void close() throws IOException {
                log.info("close");
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "ok";
    }


    @Override
    public Boolean uploadWithJar(String project, String name, String tag, MultipartFile dockerFile, MultipartFile jarFile) {
        log.info("开始");
        CountDownLatch latch = new CountDownLatch(1);
        try {
            // docker中jar打成镜像
            Boolean Boolean = jarToImage(name, tag, dockerFile, jarFile);
            // 打镜像标签，并推送到harbor镜像仓库
            // 即 镜像:标签版本
            String imageId = name + ":" + tag;
            String imageNameWithRepository = dockerConfigProperties.getRegistryHost() + "/" + project + "/" + name;
            if (Boolean != true) {
                throw new SystemException("创建镜像错误");
            }
            log.info("镜像打标签开始");
            // 打镜像标签
            dockerClient.tagImageCmd(imageId, imageNameWithRepository, tag).exec();
            // 推送镜像到harbor仓库
            String repository = imageNameWithRepository + ":" + tag;
            log.info("打标签后的镜像名：{}", repository);
            dockerClient.pushImageCmd(repository).exec(new ResultCallback<PushResponseItem>() {
                @Override
                public void onStart(Closeable closeable) {

                    log.info("推送到harbor仓库开始");
                }

                @Override
                public void onNext(PushResponseItem object) {

                }


                @Override
                public void onError(Throwable throwable) {
                    log.error("onError:{}", throwable.getMessage());
                    latch.countDown();

                }

                @Override
                public void onComplete() {
                    log.info("推送到harbor仓库成功");
//                    jarToImageLatch.countDown();
                    dockerClient.removeImageCmd(imageId).exec();
                    log.info("删除镜像:{} 成功", imageId);
                    dockerClient.removeImageCmd(repository).exec();
                    log.info("删除镜像:{} 成功", repository);
                    latch.countDown();
                }

                @Override
                public void close() throws IOException {

                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("结束");
        return true;

    }


    /**
     * jar ---> image
     *
     * @param name
     * @param tag
     * @param dockerFile
     * @param jarFile
     * @return
     * @throws Exception
     */
    public Boolean jarToImage(String name, String tag, MultipartFile dockerFile, MultipartFile jarFile) throws Exception {
        // 打包文件所在目录
        // 待maven打包测试 测试成功
        // 用uuid区分不同用户
        String subPath = UUID.randomUUID().toString();
        File fileD = new File("api-native-cloud/native-cloud-service/src/main/resources/tar/" + subPath);
        // 不存在目录则创建
        if (!fileD.exists()) {
            fileD.mkdirs();
        }
        log.info("dockerfile和jar包存放的路径为:{}", fileD.getAbsolutePath());

        // dockerFile文件保存到打包目录
        if (dockerFile.isEmpty()) {
            log.error("dockerFile不能为空");
            throw new SystemException("dockerFile不能为空");
        }
        // 开始上传Dockerfile
        log.info("开始上传Dockerfile");
        // Dockerfile保存位置为resources目录下的tar
        File fileDockerFile = new File(fileD.getPath() + "/" + dockerFile.getOriginalFilename());
        log.info("dockerfile存路经：{}", fileDockerFile.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(fileDockerFile);
        int size = 8192;
        byte[] bufferDockerfile = new byte[size];
        int isRead = 0;
        InputStream dockerFileInputStream = dockerFile.getInputStream();
        while ((isRead = dockerFileInputStream.read(bufferDockerfile, 0, bufferDockerfile.length)) != -1) {
            fileOutputStream.write(bufferDockerfile, 0, isRead);
        }
        // 关流
        fileOutputStream.close();
        dockerFileInputStream.close();
        log.info("上传Dockerfile成功");


        // jar文件保存到打包目录
        if (jarFile.isEmpty()) {
            log.error("jar文件保存到打包目录错误");
            throw new SystemException("jar文件保存到打包目录错误");
        }
        log.info("开始上传jar包");
        InputStream jarInputStream = jarFile.getInputStream();
        // Dockerfile保存位置为resources目录下的tar
        File jarDockerFile = new File(fileD.getPath() + "/" + "app.jar");
        log.info("dockerfile存路经：{}", jarDockerFile.getAbsolutePath());
        FileOutputStream jarfileOutputStream = new FileOutputStream(jarDockerFile);
        int sizeJar = 4096;
        byte[] bufferJar = new byte[sizeJar];
        int isReadJar = 0;
        while ((isReadJar = jarInputStream.read(bufferJar, 0, bufferJar.length)) != -1) {
            jarfileOutputStream.write(bufferJar, 0, isReadJar);
        }
        // 关流
        jarfileOutputStream.close();
        jarInputStream.close();
        log.info("上传Jar成功");

        String dockerTag = name + ":" + tag;
        log.info("dockerImageName ---> {}", dockerTag);
        Set<String> tags = new HashSet<>();
        tags.add(dockerTag);

        String imageId = dockerClient.buildImageCmd(fileD).withTags(tags).exec(new BuildImageResultCallback()).awaitImageId();
        log.info("imageId:{}", imageId);



        log.info("jar打成镜像结束");
        // 删除在服务器中的打包文件
        log.info("删除在服务器中的打包文件开始");
//        fileDockerFile.delete();
//        jarDockerFile.delete();
//        fileD.delete();
        FileUtils.deleteDirectory(fileD);
        log.info("删除在服务器中的打包文件成功");
        return true;
    }


    // 获取压缩包下镜像的第一个tag
    private String getImageTag(InputStream inputStream) {

        try {
            log.info("开始解image的tar包");
            TarArchiveInputStream tin = new TarArchiveInputStream(inputStream);
            TarArchiveEntry entry = tin.getNextTarEntry();
            Gson gson = new Gson();
            String json = null;
            while (entry != null) {
                // 只读取manifest.json
                if (entry.getName().equals("manifest.json")) {
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = tin.read(data, 0, 1024)) != -1) {
                        result.write(data, 0, count);
                    }
                    result.close();
                    json = result.toString();
                    break;
                }
                entry = tin.getNextTarEntry();
            }
            log.info("解image的tar包结束");
            if (json == null) {
                log.error("json为空");
                throw new SystemException("war镜像包解析错误");
            }
            // 转换为自己的Manifest类
            List<Manifest> manifests = gson.fromJson(json, new TypeToken<List<Manifest>>() {
            }.getType());
            // 获取压缩包下镜像的tag
            String imageTag = manifests.get(0).getRepoTags().get(0);
            return imageTag;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
