package edu.java.botclient;

import model.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("updates")
public interface UpdatesClient {
    @PostExchange
    ResponseEntity post(LinkUpdate request);

}

