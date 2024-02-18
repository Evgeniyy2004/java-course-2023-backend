package edu.java.configuration;

import edu.java.clients.GitHubClient;
import org.springframework.context.annotation.Bean;
import edu.java.clients.StackOverflowClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfiguration {

    @Bean
    public StackOverflowClient beanStack() {
        RestClient restClient = RestClient.builder().baseUrl("https://api.stackexchange.com/").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public GitHubClient beanGit() {
        RestClient restClient = RestClient.builder().baseUrl("https://api.github.com/").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

}

