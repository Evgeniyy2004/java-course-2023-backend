package edu.java.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
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

    private final Counter counter;

    @Autowired
    private ScrapperChatClient chat;

    private static final String REGISTRY = "Для отслеживания ссылок вам необходимо зарегистрироваться.";
    private static final String INCORRECT = "Некорректные параметры запроса";
    private static final String SUCCESS_ADD = "Ссылка успешно добавлена";

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
        Pattern pattern3 = Pattern.compile("(/track\\s+)(.+)");
        Pattern pattern4 = Pattern.compile("(/untrack\\s+)(.+)");
        String text;
        if (command.equals("/help")) {
            this.execute(new SendMessage(id, help()));
        } else if (command.equals("/start")) {
            start(id);
        } else if (!pattern3.matcher(command).find() && !pattern4.matcher(command).find()) {
            text = "Команда не распознана. Введите /help, чтобы ознакомиться с допустимыми командами.";
            this.execute(new SendMessage(id, text));
        } else {
            if (command.equals("/list")) {
                list(id);
            } else {
                var link = command.split("/untrack|/track| ");
                if (link.length == 0) {
                    this.execute(new SendMessage(id, INCORRECT));
                } else {
                    link = command.split(" ");
                    if (pattern3.matcher(command).find()) {
                        add(id, link[1]);
                    } else {
                        remove(id, link[1]);
                    }
                }
            }
        }
        counter.increment();
    }

    public void remove(Long id, String link) {
        String text;
        var req = new RemoveLinkRequest();
        req.setLink(link);
        var done = links.delete(id, req);
        if (done.getStatusCode() == HttpStatus.NOT_FOUND) {
            text = REGISTRY;
        } else if (done.getStatusCode() == HttpStatus.CONFLICT) {
            text = "Ссылка не отслеживается.";
        } else {
            text = "Ссылка удалена.";
        }
        this.execute(new SendMessage(id, text));
    }

    public void start(Long id) {
        String text;
        var entity = chat.post(id);
        if (entity.getStatusCode() == HttpStatus.CONFLICT) {
            text = "Вы не можете быть зарегистрированы повторно";
        } else {
            text = "Вы успешно зарегистрировались";
        }
        var res = new SendMessage(id, text);
        this.execute(res);
    }

    public String help() {
        return """
            /start - регистрация в боте
            /help - список доступных команд
            /track - добавление ресурса в отслеживаемые
            /untrack - прекращение отслеживания ресурса
            /list - список отслеживаемых ресурсов""";
    }

    public void add(Long id, String link) {
        try {
            var uri = new URI(link);
            var link1 = uri.toString();
            if (!link1.startsWith(BASEGIT) && !link1.startsWith(BASESTACK)) {
                this.execute(new SendMessage(id, INCORRECT));
            }
            if (link1.startsWith(BASEGIT)) {
                repo(link1, id);
            } else {
                question(link1, id, link);
            }

        } catch (URISyntaxException e) {
            this.execute(new SendMessage(id, INCORRECT));
        }
    }

    public void list(Long id) {
        StringBuilder text;
        var all = links.get(id);
        if (all.getStatusCode() == HttpStatus.NOT_FOUND) {
            text = new StringBuilder(REGISTRY);
        } else {
            var map = (LinkedHashMap) all.getBody();
            var body = (List<LinkResponse>) (map.get("links"));
            if (body == null || (body).isEmpty()) {
                text = new StringBuilder("Текущий список ссылок пуст");
            } else {
                text = new StringBuilder("Текущий список отслеживаемых ссылок:\n");
                for (LinkResponse l : body) {
                    text.append(l.getUrl()).append("\n");
                }
            }
        }
        var res = new SendMessage(id, text.toString());
        this.execute(res);
    }

    public void question(String link1, Long id, String link) {
        String text;
        var question = link1.replace(BASESTACK, "")
            .split("/");
        if (question.length == 0) {
            text = INCORRECT;
            this.execute(new SendMessage(id, text));
            return;
        }
        var id1 = Long.parseLong(question[0].toString());
        var result = stack.fetchQuestion(id1);
        if (result.time == null || result.link == null || result.title == null) {
            this.execute(new SendMessage(id, INCORRECT));
        } else {
            var req = new AddLinkRequest();
            req.setLink(link);
            var response = links.post(id, req);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                text = REGISTRY;
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                text = ALREADY;
            } else {
                text = SUCCESS_ADD;
            }
            this.execute(new SendMessage(id, text));
        }
    }

    public void repo(String link1, Long id) {
        String text;
        var userRepo = link1.strip().replace(BASEGIT, "")
            .split("/");
        if (userRepo.length < 2) {
            this.execute(new SendMessage(id, INCORRECT));
            return;
        }
        var user = userRepo[0];
        var repo = userRepo[1];
        try {
            git.fetchRepository(user, repo);
            var req = new AddLinkRequest();
            req.setLink(link1);
            var done = links.post(id, req);
            if (done.getStatusCode() == HttpStatus.NOT_FOUND) {
                text = REGISTRY;
                this.execute(new SendMessage(id, text));
            }
            if (done.getStatusCode() == HttpStatus.CONFLICT) {
                text = ALREADY;
                this.execute(new SendMessage(id, text));
            }
            if (done.getStatusCode() == HttpStatus.OK) {
                text = SUCCESS_ADD;
                this.execute(new SendMessage(id, text));
            }
        } catch (WebClientResponseException e) {
            text = "Недостаточно прав для доступа";
            this.execute(new SendMessage(id, text));
        }
    }
}

