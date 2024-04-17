package io.swagger.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class DlqErrorHandler {

    @Value("${app1.error-topic}")
    private String topic;

    private final KafkaTemplate<Integer, String> template;

    public DlqErrorHandler(KafkaTemplate<Integer, String> template) {
        this.template = template;
    }

    public void handle(String update) {
        template.send(topic, "Invalid message: " + update);
    }
}
