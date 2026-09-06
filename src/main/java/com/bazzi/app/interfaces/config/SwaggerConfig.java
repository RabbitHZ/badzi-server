package com.bazzi.app.interfaces.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // servers를 지정하지 않으면 springdoc이 현재 요청이 들어온 서버를
        // base URL로 자동 사용한다. 덕분에 로컬/배포 환경 어디서든 Swagger의
        // "Try it out"이 항상 올바른 서버로 요청을 보낸다.
        return new OpenAPI()
                .info(new Info()
                        .title("조회수 및 뱃지 관리 API")
                        .version("1.0.0")
                        .description("조회수 관리와 뱃지 생성을 위한 RESTful API"));
    }
}
