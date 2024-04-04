package io.swagger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class DlqErrorHandler {
    @Autowired
    KafkaTemplate<Integer, String> template;

    @Value("${app1.error-topic}")
    String topic;

    public void handle(String update) {
        template.send(topic, "Invalid message: " + update);
    }
}
