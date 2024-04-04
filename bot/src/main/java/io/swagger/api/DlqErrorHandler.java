package io.swagger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DlqErrorHandler {
    @Autowired
    KafkaTemplate<Integer, String> template;

    public void handle(String update) {
        template.send("2", "Invalid message: " + update);
    }
}
