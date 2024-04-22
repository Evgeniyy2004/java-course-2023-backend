package edu.java.scrapperclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("tg-chat/{id}")
public interface ScrapperChatClient {

    @PostExchange
    ResponseEntity post(@PathVariable("id") Long id);

    @DeleteExchange
    ResponseEntity<?> delete(@PathVariable Long id);

}
