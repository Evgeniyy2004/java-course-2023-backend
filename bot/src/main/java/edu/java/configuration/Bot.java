package edu.java.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@SuppressWarnings({"ReturnCount", "CyclomaticComplexity", "RegexpSinglelineJava"})
public class Bot extends TelegramBot {

    private static final String BASESTACK = "https://stackoverflow.com/questions/";
    private static final String BASEGIT = "https://github.com/";

    private static final String SERVER_ERROR = "Ошибка на сервере. Повторите попытку позднее.";

    private static final String SUCCESSREGISTER = "Чат зарегистрирован";
    private static final String TOO_MANY = "Слишком большая нагрузка на сервер. Повторите попытку позднее.";
    private Counter counter;

    @Autowired
    private ScrapperChatClient chat;

    private static final String REGISTRY = "Для отслеживания ссылок вам необходимо зарегистрироваться.";
    private static final String INCORRECT = "Некорректные параметры запроса";

    @Autowired
    private ScrapperLinksClient links;
    private static final String ALREADY = "Ссылка уже отслеживается";

    @Autowired
    private GitHubClient git;

    @Autowired
    private StackOverflowClient stack;

    public Bot(MeterRegistry registry) {
        super(System.getenv("APP_TELEGRAM_TOKEN"));
        this.counter =
            Counter.builder("processed_messages").description("Number of processed messages").register(registry);
        this.setUpdatesListener(updates -> {
            for (Update update : updates) {
                this.handle(update);
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
    }

    public void handle(Update update) {
        var command = update.message().text();
        var id = update.message().chat().id();
        Pattern pattern1 = Pattern.compile("(\\s*)(/list)(\\s*)");
        Pattern pattern2 = Pattern.compile("(\\s*)(/start)(\\s*)");
        Pattern pattern3 = Pattern.compile("(\\s*)(/track\\s+)(.*)");
        Pattern pattern4 = Pattern.compile("(\\s*)(/untrack\\s+)(.*)");
        Pattern pattern5 = Pattern.compile("(\\s*)(/help)(\\s*)");
        var res = new SendMessage(update.message().chat().id(), "");
        String text;
        if (pattern5.matcher(command).find()) {
            res = new SendMessage(update.message().chat().id(), help());
            this.execute(res);
            counter.increment();
            return;
        } else if (pattern2.matcher(command).find()) {
            try {
                chat.post(update.message().chat().id());
                res = new SendMessage(id, SUCCESSREGISTER);
                counter.increment();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.CONFLICT) {
                    res = new SendMessage(id, "Вы уже зарегистрированы");
                    counter.increment();
                } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    res = new SendMessage(id, TOO_MANY);
                } else {
                    res = new SendMessage(id, SERVER_ERROR);
                }
            }
            this.execute(res);
            return;
        }

        if (!pattern1.matcher(command).find() && !pattern2.matcher(command).find()
            && !pattern3.matcher(command).find() && !pattern4.matcher(command).find()
            && !pattern5.matcher(command).find()) {
            text = "Команда не распознана."
                + "Введите /help, чтобы ознакомиться с допустимыми командами.";
            res = new SendMessage(update.message().chat().id(), text);
            this.execute(res);
            counter.increment();
        } else {
            if (pattern1.matcher(command).find()) {
                try {
                    var all = links.get(update.message().chat().id());
                    var body = (ListLinksResponse) all.getBody();
                    if (body.getLinks().isEmpty()) {
                        text = "Текущий список ссылок пуст";
                    } else {
                        text = "Текущий список отслеживаемых ссылок:\n";
                        for (LinkResponse l : body.getLinks()) {
                            text += l.getUrl() + "\n";
                        }
                    }
                    res = new SendMessage(update.message().chat().id(), text);
                    counter.increment();
                } catch (WebClientResponseException e) {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        res = new SendMessage(id, REGISTRY);
                        counter.increment();
                    } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        res = new SendMessage(id, TOO_MANY);
                    } else {
                        res = new SendMessage(id, SERVER_ERROR);
                    }
                }
                this.execute(res);
            } else {
                if (pattern3.matcher(command).find()) {
                    var link = Arrays.stream(command.split("/track")).filter(x -> x != "").toArray(String[]::new);
                    if (link.length == 0) {
                        incorrect(id);
                    } else {
                        check(update.message().chat().id(), link[0]);
                    }
                } else {
                    var link = Arrays.stream(command.split("/untrack")).filter(x -> x != "").toArray(String[]::new);
                    if (link.length == 0) {
                        incorrect(id);
                        return;
                    }
                    var req = new RemoveLinkRequest();
                    req.setLink(link[0]);
                    try {
                        links.delete(id, req);
                        text = "Ссылка удалена.";
                        counter.increment();
                    } catch (WebClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            text = REGISTRY;
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
                            text = "Ссылка не отслеживается.";
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                            text = TOO_MANY;
                        } else {
                            text = SERVER_ERROR;
                        }
                    }

                    this.execute(new SendMessage(id, text));
                }
            }

        }

    }

    public String help() {
        return """
            /start - регистрация в боте
            /help - список доступных команд
            /track - добавление ресурса в отслеживаемые
            /untrack - прекращение отслеживания ресурса
            /list - список отслеживаемых ресурсов""";
    }

    public void check(Long id, String link) {
        String text;
        var success = "Ссылка успешно добавлена";
        if (!link.startsWith(BASEGIT) && !link.startsWith(BASESTACK)) {
            incorrect(id);
        } else {
            try {
                var uri = new URI(link);
                var link1 = uri.toString();
                if (link1.startsWith(BASEGIT)) {
                    var userrepo = Arrays.stream(link1.replace(BASEGIT, "").split("/")).filter(x -> x != "").toArray(String[]::new);
                    if (userrepo.length < 2) {
                        text = INCORRECT;
                        this.execute(new SendMessage(id, text));
                        return;
                    }
                    var user = userrepo[0];
                    var repo = userrepo[1];
                    var result = git.fetchRepository(user, repo);
                    if (result.name == null || result.owner == null || result.time == null) {
                        incorrect(id);
                        return;
                    }
                    var req = new AddLinkRequest();
                    req.setLink(link1);
                    try {
                        links.post(id, req);
                        text = success;
                        counter.increment();
                    }
                    catch (WebClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            text = REGISTRY;
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
                            text = ALREADY;
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                            text = TOO_MANY;
                        } else {
                            text = SERVER_ERROR;
                        }
                    }
                    this.execute(new SendMessage(id, text));
                    return;
                }
                var question = Arrays.stream(link1.replace(BASESTACK, "").split("/")).filter(x -> x != "").toArray(String[]::new);
                if (question.length < 2) {
                    text = INCORRECT;
                    this.execute(new SendMessage(id, text));
                    return;
                }
                var id1 = Long.parseLong(question[0]);
                var result = stack.fetchQuestion(id1);
                if (result.time == null || result.link == null || result.title == null) {
                    incorrect(id);
                } else {
                    var req = new AddLinkRequest();
                    req.setLink(link1);
                    try {
                        links.post(id, req);
                        text = success;
                        counter.increment();
                    }
                    catch (WebClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            text = REGISTRY;
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
                            text = ALREADY;
                            counter.increment();
                        } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                            text = TOO_MANY;
                        } else {
                            text = SERVER_ERROR;
                        }
                    }
                    this.execute(new SendMessage(id, text));
                }
            } catch (URISyntaxException e) {
                text = INCORRECT;
                this.execute(new SendMessage(id, text));
            }
        }
    }

    public void incorrect(Long id) {
        var text = INCORRECT;
        counter.increment();
        this.execute(new SendMessage(id, text));
    }
}

