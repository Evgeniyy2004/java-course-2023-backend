package edu.java.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("")
public interface GitHubClient {
    RepoResponse fetchRepository(@PathVariable String user, @PathVariable String repo);
}
