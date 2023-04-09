package com.fsc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
public class ApplicationStarter implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initK8sClient();
    }

    // 初始化fabric8-K8sClient
    private void initK8sClient() {
        log.info("加载ApplicationStarter类成功");
    }
}
