package com.example.mrsaccountant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.mrsaccountant.config.Config;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MrsAccountantApplication {

    public static void main(String[] args) {

        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile == null || activeProfile.isEmpty()) {
            activeProfile = "dev";
        }

       
        if (!activeProfile.equals("prod")) {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println(".env file loaded for profile: " + activeProfile);

        } else {
            System.out.println("Using environment variables for profile: prod");
        }
        
        System.setProperty("spring.profiles.active", activeProfile);

        SpringApplication.run(MrsAccountantApplication.class, args);

        
    }
}
