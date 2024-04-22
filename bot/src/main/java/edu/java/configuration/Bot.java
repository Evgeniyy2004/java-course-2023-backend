package edu.java.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@EnableConfigurationProperties({ApplicationConfig.class})
@SuppressWarnings({"ReturnCount", "CyclomaticComplexity", "RegexpSinglelineJava"})
public class Bot extends TelegramBot {

    private static final String BASESTACK = "https://stackoverflow.com/questions/";
    private static final String BASEGIT = "https://github.com/";

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

    public Bot(ApplicationConfig app) {
        super(app.telegramToken());
    }

    public void handle(Update update) {
        var command = update.message().text();
        var id = update.message().chat().id();
        Pattern pattern1 = Pattern.compile("( *)(/list)( *)");
        Pattern pattern2 = Pattern.compile("( *)(/start)( *)");
        Pattern pattern3 = Pattern.compile("( *)(/track)( *)(.*)");
        Pattern pattern4 = Pattern.compile("( *)(/untrack)( *)(.*)");
        Pattern pattern5 = Pattern.compile("( *)(/help)( *)");
        var res = new SendMessage(update.message().chat().id(), "");
        String text="";
        if (pattern5.matcher(command).find()) {
            res = new SendMessage(update.message().chat().id(), help());
            this.execute(res);
            return;
        } else if (pattern2.matcher(command).find()) {
            try {
                var entity = chat.post(update.message().chat().id());
                text = "Вы успешно зарегистрировались";
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.CONFLICT) {
                    text = "Вы не можете быть зарегистрированы повторно";
                } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    text = "Непредвиденная ошибка. Повторите попытку позднее";
                }
            }
            res = new SendMessage(update.message().chat().id(), text);
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
        } else {
            if (pattern1.matcher(command).find()) {
                var all = links.get(update.message().chat().id());
                if (all.getStatusCode() == HttpStatus.NOT_FOUND) {
                    text = REGISTRY;
                } else {
                    var body = (ListLinksResponse) all.getBody();
                    if (body.getLinks().isEmpty()) {
                        text = "Текущий список ссылок пуст";
                    } else {
                        text = "Текущий список отслеживаемых ссылок:\n";
                        for (LinkResponse l : body.getLinks()) {
                            text += l.getUrl() + "\n";
                        }
                    }
                }
                res = new SendMessage(update.message().chat().id(), text);
                this.execute(res);

            } else {
                if (pattern3.matcher(command).find()) {
                    var link = command.split("/track");
                    if (link.length == 0) {
                        incorrect(id);
                    } else {
                        check(update.message().chat().id(), link[0]);
                    }
                } else {
                    var link = command.split("/untrack");
                    if (link.length == 0) {
                        incorrect(id);
                        return;
                    }
                    var req = new RemoveLinkRequest();
                    req.setLink(link[0]);
                    var done = links.delete(id, req);
                    if (done.getStatusCode() == HttpStatus.NOT_FOUND) {
                        text = REGISTRY;
                        this.execute(new SendMessage(id, text));
                    } else if (done.getStatusCode() == HttpStatus.CONFLICT) {
                        text = "Ссылка не отслеживается.";
                        this.execute(new SendMessage(id, text));
                    } else {
                        text = "Ссылка удалена.";
                        this.execute(new SendMessage(id, text));
                    }
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
                    var userrepo = link1.replace(BASEGIT, "").split("/");
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
                        text = success;
                        this.execute(new SendMessage(id, text));
                    }
                    return;

                }
                var question = link1.replace(BASESTACK, "").split("/");
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
                    var response = links.post(id, req);
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        text = REGISTRY;
                    } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                        text = ALREADY;
                    } else {
                        text = success;
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
        this.execute(new SendMessage(id, text));
    }
}

