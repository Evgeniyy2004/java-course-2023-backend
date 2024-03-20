package edu.java.scrapperclient;

import io.swagger.model.AddLinkRequest;
import io.swagger.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("links")
public interface ScrapperLinksClient {
    @PostExchange
    ResponseEntity post(Long id, AddLinkRequest request);

    @GetExchange
    ResponseEntity get(Long id);
    @DeleteExchange
    ResponseEntity delete(Long id, RemoveLinkRequest request);
}
