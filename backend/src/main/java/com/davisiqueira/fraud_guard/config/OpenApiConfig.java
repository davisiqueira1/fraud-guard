package com.davisiqueira.fraud_guard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fraud Guard API")
                        .description("Transaction analysis system with fraud alerts and a robust architecture in Spring Boot + AWS.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Davi Siqueira")
                                .email("davisvsiqueira@gmail.com")
                                .url("https://davisiqueira1.github.io/")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local")
                ));
    }
}
