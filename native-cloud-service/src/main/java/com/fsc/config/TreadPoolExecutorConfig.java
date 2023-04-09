package com.fsc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class TreadPoolExecutorConfig {
    // 方法一 实现implements AsyncConfigurer

//    @Override
//    public Executor getAsyncExecutor() {
//        return AsyncConfigurer.super.getAsyncExecutor();
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
//    }

    // 方法二 bean注入
    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor(){
        log.info("《===加载docker异步线程池配置开始===》");
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int processNum = Runtime.getRuntime().availableProcessors();//vm 虚拟机核数
        log.info("processNum:{}",processNum);
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        threadPoolExecutor.setCorePoolSize(corePoolSize); // 核心池大小
        log.info("corePoolSize:{}",corePoolSize);
        threadPoolExecutor.setMaxPoolSize(maxPoolSize); // 最大线程数
        log.info("maxPoolSize:{}",maxPoolSize);
        threadPoolExecutor.setQueueCapacity(maxPoolSize * 1000); // 队列程度
        log.info("QueueCapacity:{}",maxPoolSize * 1000);
        threadPoolExecutor.setThreadPriority(Thread.MAX_PRIORITY);
        threadPoolExecutor.setDaemon(false);
        threadPoolExecutor.setKeepAliveSeconds(300);// 线程空闲时间
        log.info("线程名字前缀: docker-Executor-");
        threadPoolExecutor.setThreadNamePrefix("docker-Executor-"); // 线程名字前缀
        log.info("《===加载docker异步线程池配置成功===》");
        return threadPoolExecutor;
    }


}
