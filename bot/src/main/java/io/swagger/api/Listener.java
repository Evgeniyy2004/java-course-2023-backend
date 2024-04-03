package io.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.configuration.Bot;
import edu.java.configuration.ClientConfiguration;
import edu.java.model.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "app1", ignoreUnknownFields = false)
public class Listener {
    @Autowired
    Bot bot;

    @KafkaListener(topics = "${app1.topic}")
    public void listen(String update) {
        try {
            LinkUpdate obj = new ObjectMapper().readValue(update, LinkUpdate.class);
            bot.execute(new SendMessage(obj.getId(), "По ссылке " + obj.getUrl() + "появилось обновление"));
        } catch (JsonProcessingException e) {

        }
    }

    public void send(LinkUpdate body){
        bot.execute(new SendMessage(body.getId(), "По ссылке " + body.getUrl() + "появилось обновление"));
    }

}
