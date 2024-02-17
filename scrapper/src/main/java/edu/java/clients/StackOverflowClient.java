package edu.java.clients;

import jakarta.websocket.server.PathParam;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("")
public interface StackOverflowClient {
        QuestionResponse fetchQuestion(@PathVariable long id);
}
