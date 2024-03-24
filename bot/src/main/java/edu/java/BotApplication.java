package edu.java;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.Bot;
import edu.java.configuration.ClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfiguration.class})
public class BotApplication {
    public static void main(String[] args) {

        SpringApplication.run(BotApplication.class, args);
    }
}
