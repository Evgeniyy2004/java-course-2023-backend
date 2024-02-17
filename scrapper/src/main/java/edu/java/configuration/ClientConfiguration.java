package edu.java.configuration;

import edu.java.clients.QuestionResponse;
import org.springframework.context.annotation.Bean;
import edu.java.clients.StackOverflowClient;
import java.net.URI;
import java.net.URL;

public class ClientConfiguration {
    @Bean
    public StackOverflowClient beanStack(URL ... url){
        return new StackOverflowClient() {

            @Override
            public QuestionResponse fetchQuestion(long value) {
                if (value == 0L)
            }
        };
    }
}
