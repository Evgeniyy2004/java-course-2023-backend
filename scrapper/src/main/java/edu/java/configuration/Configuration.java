package edu.java.configuration;

import edu.java.botclient.UpdatesClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@org.springframework.context.annotation.Configuration
@SpringBootConfiguration
@ComponentScan
public class Configuration {

    @Bean
    public StackOverflowClient beanStack() {
        WebClient restClient = WebClient.builder().baseUrl("https://api.stackexchange.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public GitHubClient beanGit() {
        WebClient restClient = WebClient.builder().baseUrl("https://api.github.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public UpdatesClient beanUpdates() {
        WebClient restClient = WebClient.builder().baseUrl("http://localhost:8080/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UpdatesClient.class);
    }





    @Bean
    public JdbcTemplate template() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5437/scrapper");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return new JdbcTemplate(dataSource);
    }



}

