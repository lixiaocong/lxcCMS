package com.lixiaocong.cms.Dev;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Profile("dev")
public class CorsConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("x-requested-with","x-auth-token","content-type")
                .maxAge(3600)
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE","HEAD")
                .allowCredentials(true);
    }
}
