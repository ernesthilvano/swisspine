package com.swisspine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * 
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 * Access API docs at: http://localhost:8080/api-docs
 * 
 * @author SwissPine Engineering Team
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Connection Planner API")
                        .version("1.0.0")
                        .description("""
                                External connections and data planner management system.

                                This API provides comprehensive functionality for:
                                - Managing external system connections with secure credential storage
                                - Creating and managing data planners with sources, runs, and reports
                                - Managing master data (source names, run names, report types, funds)
                                - Server-side search and pagination for all list endpoints
                                """)
                        .contact(new Contact()
                                .name("SwissPine Engineering Team")
                                .email("engineering@swisspine.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://swisspine.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server"),
                        new Server()
                                .url("https://api.swisspine.com")
                                .description("Production server")));
    }
}
