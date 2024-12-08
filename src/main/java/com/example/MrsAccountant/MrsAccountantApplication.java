package com.example.mrsaccountant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MrsAccountantApplication {

    public static void main(String[] args) {
        // 獲取 SPRING_PROFILES_ACTIVE 的值，如果沒有則默認為 "dev"
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile == null || activeProfile.isEmpty()) {
            activeProfile = "dev";
        }

        System.out.println("Active Profile: " + activeProfile);

        // 如果不是 "prod" 環境，則載入 .env 文件的變數
        if (!activeProfile.equals("prod")) {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println(".env file loaded for profile: " + activeProfile);
        } else {
            System.out.println("Using environment variables for profile: prod");
        }

        // 設定 Spring 的活動 profile
        System.setProperty("spring.profiles.active", activeProfile);

        // 啟動 Spring Boot 應用
        SpringApplication.run(MrsAccountantApplication.class, args);
    }
}
