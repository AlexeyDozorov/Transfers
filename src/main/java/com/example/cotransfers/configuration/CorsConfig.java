package com.example.cotransfers.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")

                .allowCredentials(true)
                .exposedHeaders("*")

     //           .allowedOrigins("*")

                //TODO Убрать в проде localhost

               .allowedOriginPatterns("*", "http://localhost:3000");
    }
}
