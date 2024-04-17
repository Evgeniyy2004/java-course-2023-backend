package io.swagger.api;

import edu.java.configuration.ApplicationConfig;
import edu.java.model.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    @Autowired
    private KafkaTemplate<Integer, String> template;

    public void send(LinkUpdate update) {
        // TODO
        template.send(ApplicationConfig.getTopic(), update.toString());
    }
}
