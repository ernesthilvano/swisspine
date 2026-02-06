package com.swisspine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application entry point for the Connection Planner service.
 * 
 * This service manages external system connections and data planner
 * configurations
 * with support for expandable UI components, server-side search, and
 * pagination.
 * 
 * @author SwissPine Engineering Team
 * @version 1.0.0
 */
@SpringBootApplication
public class ConnectionPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionPlannerApplication.class, args);
    }
}
