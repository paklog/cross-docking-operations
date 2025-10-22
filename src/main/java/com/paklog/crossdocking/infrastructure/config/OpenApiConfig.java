package com.paklog.crossdocking.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI crossDockOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Cross-Docking Operations API")
                .description("Cross-docking flow optimization for Paklog WMS/WES")
                .version("1.0.0"));
    }
}
