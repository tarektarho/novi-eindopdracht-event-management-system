package nl.novi.event_management_system;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagementSystemApplication {
    static {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }
    public static void main(String[] args) {
        SpringApplication.run(EventManagementSystemApplication.class, args);
    }

}
