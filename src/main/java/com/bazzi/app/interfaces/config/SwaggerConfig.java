package com.bazzi.app.interfaces.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("조회수 및 뱃지 관리 API")
                        .version("1.0.0")
                        .description("조회수 관리와 뱃지 생성을 위한 RESTful API"));
    }
}
