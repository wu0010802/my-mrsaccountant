package com.example.mrsaccountant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MrsAccountantApplication {

    public static void main(String[] args) {

        // 獲取 SPRING_PROFILES_ACTIVE 的值，默認為空字符串
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile == null || !activeProfile.equals("prod")) {
            // 加載 .env 文件
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println(".env file loaded for local environment.");
        } else {
            System.out.println("Detected active profile: " + activeProfile);
        }

        SpringApplication.run(MrsAccountantApplication.class, args);
    }
}
