package com.bazzi.app.interfaces.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server httpsServer = new Server();
        httpsServer.setUrl("https://bazzi-server-464152216340.asia-northeast3.run.app");
        httpsServer.setDescription("Production Server (HTTPS)");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        return new OpenAPI()
                .servers(Arrays.asList(httpsServer, localServer))
                .info(new Info()
                        .title("조회수 및 뱃지 관리 API")
                        .version("1.0.0")
                        .description("조회수 관리와 뱃지 생성을 위한 RESTful API"));
    }
}
