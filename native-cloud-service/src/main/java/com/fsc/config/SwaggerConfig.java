//package com.fsc.config;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author rongrong
// * @version 1.0
// * @description Swagger3配置
// * @date 2021/01/12 21:00
// */
////@Configuration
////@Profile({"dev", "local"})
//@Component
//@EnableOpenApi
//@Slf4j
//public class SwaggerConfig {
//
//    /**
//     * 是否开启swagger配置，生产环境需关闭
//     */
//    /*    @Value("${swagger.enabled}")*/
//    @Value("${swagger.enabled}")
//    private boolean enable;
//
//    /**
//     * 创建API
//     * http:IP:端口号/swagger-ui/index.html 原生地址
//     * http:IP:端口号/doc.html bootStrap-UI地址
//     */
//    @Bean
//    public Docket createRestApi() {
//        log.info("enableSwagger3:{}", enable);
//        return new Docket(DocumentationType.OAS_30)
////                .pathMapping("/")
//                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
//                .enable(enable)
//                .apiInfo(apiInfo())
//                // 设置哪些接口暴露给Swagger展示
//                .select()
//                // 扫描所有有注解的api，用这种方式更灵活
////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // 扫描所有包下
//                .apis(RequestHandlerSelectors.basePackage("com.fsc.controller"))
//                // 扫描所有 .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.regex("(?!/ApiError.*).*"))
//                .paths(PathSelectors.any())
//                .build();
//
//    }
//
//
//    /**
//     * 添加摘要信息
//     *
//     * @return 返回ApiInfo对象
//     */
//    private ApiInfo apiInfo() {
//        // 用ApiInfoBuilder进行定制
//        return new ApiInfoBuilder()
//                // 设置标题
//                .title("接口文档")
//                // 描述
//                .description("这是SWAGGER_3生成的接口文档")
//                // 版本
//                .version("版本号:V1.0")
//                //协议
//                .license("The Apache License")
//                // 协议url
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
//                .build();
//    }
//}
