package com.fsc;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PushResponseItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.Closeable;
import java.io.IOException;

@SpringBootTest
class ApplicationTest {
    @Autowired
    private DockerClient dockerClient;
    @Test
    void contextLoads() {
    }

    @Test
    void test(){
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject("http://192.168.200.131:30002/api/v2.0/projects", String.class);
        System.out.println(forObject);
    }
    @Test
    void test1(){
        dockerClient.pushImageCmd("192.168.200.131:30002/test/kube-state-metrics:v1.0").exec(new ResultCallback<PushResponseItem>() {
            @Override
            public void onStart(Closeable closeable) {

            }

            @Override
            public void onNext(PushResponseItem object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void close() throws IOException {

            }
        });
    }


}
