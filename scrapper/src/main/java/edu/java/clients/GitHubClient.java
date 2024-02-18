package edu.java.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url= "https://api.github.com/repos/{user}/{repo}")
public interface GitHubClient {
    RepoResponse fetchRepository(@PathVariable String user, @PathVariable String repo);
}
