package edu.java.configuration;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Configuration
@Validated
@ComponentScan(basePackages = "io.swagger.api")
@SuppressWarnings("RegexpSinglelineJava")
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "app1", ignoreUnknownFields = false)
public class ClientConfiguration {
    String base = "http://localhost:8080/";

    @Value("${app1.codes}")
    ArrayList<Integer> codes;

    @Value("${app1.strategy}")
    STRATEGY strategy;

    @Value("${app1.topic}")
    @Getter
    private static String topic;

    @Value("${app1.token}")
    String token;

    public enum STRATEGY {
        CONSTANT,
        EXPONENTIAL
    }



    @Bean
    public ScrapperChatClient beanChat() {
        WebClient restClient = WebClient.builder().baseUrl(base).filter(withRetryableRequests()).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScrapperChatClient.class);
    }


    @Bean
    public ScrapperLinksClient beanLinks() {
        WebClient restClient = WebClient.builder().baseUrl(base).filter(withRetryableRequests()).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScrapperLinksClient.class);
    }

    @Bean
    public GitHubClient beanGit() {
        WebClient restClient =
            WebClient.builder().baseUrl("https://api.github.com/").filter(withRetryableRequests()).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public StackOverflowClient beanStack() {
        WebClient restClient =
            WebClient.builder().baseUrl("https://api.stackexchange.com/").filter(withRetryableRequests()).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    private ExchangeFilterFunction withRetryableRequests() {
        if (strategy == STRATEGY.EXPONENTIAL) {
            return (request, next) -> next.exchange(request)
                .flatMap(clientResponse -> Mono.just(clientResponse)
                    .filter(response -> codes.contains(response.statusCode().value()))
                    .flatMap(response -> clientResponse.createException())
                    .flatMap(Mono::error)
                    .thenReturn(clientResponse))
                .retryWhen(this.retryBackoffExp());
        } else {
            return (request, next) -> next.exchange(request)
                .flatMap(clientResponse -> Mono.just(clientResponse)
                    .filter(response -> codes.contains(response.statusCode().value()))
                    .flatMap(response -> clientResponse.createException())
                    .flatMap(Mono::error)
                    .thenReturn(clientResponse))
                .retryWhen(this.retryConst());
        }
    }

    private RetryBackoffSpec retryBackoffExp() {
        return Retry.backoff(2 + 1, Duration.ofSeconds(2 + 2 + 1))
            .filter(throwable -> throwable instanceof WebClientResponseException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    private RetryBackoffSpec retryConst() {
        return (Retry.fixedDelay(2 + 1, Duration.ofSeconds(2 + 2 + 1)))
            .filter(throwable -> throwable instanceof WebClientResponseException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    @Bean
    public Bot makeBot() {
        var bot = new Bot(token);
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
