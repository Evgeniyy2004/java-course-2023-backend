package edu.java.configuration;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class ClientConfiguration {
    String base = "http://localhost:8080/";

    @Bean
    public ScrapperChatClient beanChat() {
        WebClient restClient = WebClient.builder().baseUrl(base).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScrapperChatClient.class);
    }

    @Bean
    public ScrapperLinksClient beanLinks() {
        WebClient restClient = WebClient.builder().baseUrl(base).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScrapperLinksClient.class);
    }

    @Bean
    public GitHubClient beanGit() {
        WebClient restClient = WebClient.builder().baseUrl("https://github.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public StackOverflowClient beanStack() {
        WebClient restClient = WebClient.builder().baseUrl("https://stackoverflow.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public Bot makeBot() {
        var bot = new Bot(new ApplicationConfig(System.getenv("APP_TELEGRAM_TOKEN")));
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                bot.handle(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                // god bad response from telegram
                e.response().errorCode();
                e.response().description();
            } else {
                // probably network error
                e.printStackTrace();
            }
        }, new GetUpdates().limit(2 * 2 * 2 * 2 * 2 * 2 + 2 * 2 * 2 * 2 * 2 + 2 * 2).offset(0).timeout(0));
        return bot;
    }

}
