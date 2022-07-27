package com.miniproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String IMAGEPATH = "C:/Users/HRYUN/Desktop/항해99/week06/hanghae-mini-project/src/main/resources/static/image";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        WebMvcConfigurer.super.addResourceHandlers(registry);

        //외부에 있는 폴더를 내 프로젝트에서 찾을 수 있도록 세팅
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + IMAGEPATH)
                // 업로드
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
              .allowedOriginPatterns("*")
              .allowedMethods("*")
              .allowedHeaders("*")
              .exposedHeaders("*")
              .allowCredentials(true)
              .maxAge(1600);
    }
}
