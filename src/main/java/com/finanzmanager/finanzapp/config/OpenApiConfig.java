package com.finanzmanager.finanzapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI finanzAppOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("FinanzApp API Dokumentation")
                        .description("REST API Dokumentation der FinanzApp - Transaktionen, Filter, Berichte")
                        .version("1.0.0"));
    }
}
