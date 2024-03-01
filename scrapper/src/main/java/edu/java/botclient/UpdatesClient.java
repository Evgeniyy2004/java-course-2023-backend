package edu.java.botclient;

import io.swagger.model.AddLinkRequest;
import io.swagger.model.LinkUpdate;
import io.swagger.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("updates")
public interface UpdatesClient {
    @PostExchange
    ResponseEntity post(LinkUpdate request);

}

