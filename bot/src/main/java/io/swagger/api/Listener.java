package io.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.configuration.Bot;
import edu.java.model.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.yml")
public class Listener {

    private final Bot bot;

    private final DlqErrorHandler handler;

    private static final String FIRST = "По ссылке %s появилось обновление";

    public Listener(DlqErrorHandler handler, Bot bot) {
        this.handler = handler;
        this.bot = bot;
    }

    @KafkaListener(topics = "${app1.topic}")
    public void listen(String update) {
        try {
            LinkUpdate obj = new ObjectMapper().readValue(update, LinkUpdate.class);
            bot.execute(new SendMessage(obj.getId(), String.format(FIRST ,obj.getUrl() )));
        } catch (JsonProcessingException e) {
            handler.handle(update);
        }
    }

    public void send(LinkUpdate body) {
        bot.execute(new SendMessage(body.getId(), String.format(FIRST ,body.getUrl())));
    }

}
