package com.healthtracker.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
/**
 * Punto de entrada de la aplicación Spring Boot.
 */
public class BackendApplication {

    public static void main(String[] args) {
        DotenvLoader.load();
        SpringApplication.run(BackendApplication.class, args);
    }

}
