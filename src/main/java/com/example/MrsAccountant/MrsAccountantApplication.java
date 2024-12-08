package com.example.mrsaccountant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MrsAccountantApplication {

    public static void main(String[] args) {

        
        if (System.getenv("SPRING_PROFILES_ACTIVE") == null) {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println(".env file loaded for local environment.");
        } else {
            System.out.println("Detected active profile: " + System.getenv("SPRING_PROFILES_ACTIVE"));
        }

        SpringApplication.run(MrsAccountantApplication.class, args);
    }
}
