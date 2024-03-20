package edu.java.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import edu.java.scrapperclient.ScrapperChatClient;
import edu.java.scrapperclient.ScrapperLinksClient;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Log
@Component
public class Bot extends TelegramBot {
    @Autowired
    private ScrapperChatClient chat;

    @Autowired
    private ScrapperLinksClient links;


    public Bot(ApplicationConfig app) {
        super(app.telegramToken());
    }

    public void handle(Update update) {
        var command = update.message().text();
        var id = update.message().chat().id();
        Pattern pattern1 = Pattern.compile("( *)(/list)( *)");
        Pattern pattern2 = Pattern.compile("( *)(/start)( *)");
        Pattern pattern3 = Pattern.compile("( *)(/track)( *)");
        Pattern pattern4 = Pattern.compile("( *)(/untrack)( *)");
        Pattern pattern5 = Pattern.compile("( *)(/help)( *)");
            var res = new SendMessage(update.message().chat().id(), "");
            String text;
            if (pattern5.matcher(command).find()) {
                res = new SendMessage(update.message().chat().id(), help());
                this.execute(res);
                return;
            } else if (pattern2.matcher(command).find()) {
                var entity = chat.post(update.message().chat().id());

                if (entity.getStatusCode() == HttpStatus.CONFLICT) {
                    text = "Вы не можете быть зарегистрированы повторно";
                } else text = "Вы успешно зарегистрировались";
                res = new SendMessage(id, text);
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
            }
             else {
                if (pattern1.matcher(command).find()) {
                    var str = list();
                    var all = links.get(update.message().chat().id());
                    if (all.getStatusCode() == HttpStatus.NOT_FOUND) {
                        text = "Для отслеживания ссылок вам необходимо зарегистрироваться.";
                    } else
                    {
                        var list = (ListLinksResponse)all.getBody();
                    }

                } else {
                    if (pattern3.matcher(command).find()) {
                        var answer = "Введите ссылку на ресурс, который хотите отслеживать";
                        log.info(answer);
                        res = new SendMessage(update.message().chat().id(), answer);
                    } else {
                        if (pattern4.matcher(command).find()) {
                            text = "Введите ссылку на ресурс, который хотите перестать отслеживать";
                            log.info(text);
                            res = new SendMessage(
                                update.message().chat().id(), text);
                        } else {
                            log.info(help());
                            res = new SendMessage(update.message().chat().id(), help());
                        }
                    }
                }

            }

        }

    public static String help() {
        return """
            /start - регистрация в боте
            /help - список доступных команд
            /track - добавление ресурса в отслеживаемые
            /untrack - прекращение отслеживания ресурса
            /list - список отслеживаемых ресурсов""";
    }
}

