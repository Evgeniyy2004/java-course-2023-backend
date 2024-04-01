package edu.java.scrapperclient;


import edu.java.model.AddLinkRequest;
import edu.java.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("links/{id}")
public interface ScrapperLinksClient {
    @PostExchange
    ResponseEntity<?> post(@PathVariable Long id, AddLinkRequest request);

    @GetExchange
    ResponseEntity<?> get(@PathVariable Long id);

    @DeleteExchange
    ResponseEntity<?> delete(@PathVariable Long id, RemoveLinkRequest request);

}
