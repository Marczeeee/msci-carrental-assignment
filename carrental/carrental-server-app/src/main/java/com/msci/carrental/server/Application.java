package com.msci.carrental.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Spring Boot.
 *
 */
@SpringBootApplication(scanBasePackages = "com.msci.carrental")
@EnableJpaRepositories(basePackages = "com.msci.carrental.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "com.msci.carrental.model")
public class Application {
    /**
     * Main class of the application invoking it as a Spring Boot app.
     *
     * @param args
     *            application arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
