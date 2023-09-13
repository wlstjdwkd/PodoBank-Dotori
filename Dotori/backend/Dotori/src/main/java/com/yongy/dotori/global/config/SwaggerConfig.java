package com.yongy.dotori.global.config;//package com.yongy.dotori.global.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
//import lombok.RequiredArgsConstructor;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@OpenAPIDefinition(
//        info = @Info(title = "도토리 API 명세서",
//                description = "도토리 아키텍처 기반 API 명세서",
//                version = "v1"))
//
//@RequiredArgsConstructor
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/v1/**"};
//
//        return GroupedOpenApi.builder()
//                .group("도토리 API v1")
//                .pathsToMatch(paths)
//                .build();
//    }
//}