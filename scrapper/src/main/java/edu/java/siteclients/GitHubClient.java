package edu.java.siteclients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@HttpExchange(url = "https://api.github.com/repos/{user}/{repo}", accept = APPLICATION_JSON)
public interface GitHubClient {

    @GetExchange
    RepoResponse fetchRepository(@PathVariable String user, @PathVariable String repo);
}
