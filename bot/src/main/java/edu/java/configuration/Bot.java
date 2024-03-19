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
    private ScrapperChatClient links;

    private int condition = -1;

    public Bot(ApplicationConfig app) {
        super(app.telegramToken());
    }

    public SendMessage handle(Update update) {
        var command = update.message().text();
        var id = update.message().chat().id();
        Pattern pattern1 = Pattern.compile("( *)(/list)( *)");
        Pattern pattern2 = Pattern.compile("( *)(/start)( *)");
        Pattern pattern3 = Pattern.compile("( *)(/track)( *)");
        Pattern pattern4 = Pattern.compile("( *)(/untrack)( *)");
        Pattern pattern5 = Pattern.compile("( *)(/help)( *)");
        if (condition == -1) {
            var res = new SendMessage(update.message().chat().id(), "");
            if (pattern5.matcher(command).find()) {
                res = new SendMessage(update.message().chat().id(), help());
            } else if (pattern2.matcher(command).find()) {
                var entity = chat.post(update.message().chat().id());
                String text ="";
                if (entity.getStatusCode() == HttpStatus.NOT_FOUND) {
                    text = "Вы не можете быть зарегистрированы повторно";
                } else text = "Вы успешно зарегистрировались";
                condition = 0;
                res = new SendMessage(id, text);
            } else {
                var text = "Для доступа ко всем командам зарегистрируйтесь с помощью команды /start.";
                log.info(text);
                res = new SendMessage(id, text);
            }
            this.execute(res);
            return res;
        }
        if (condition == 0) {
            String text;
            var res = new SendMessage(update.message().chat().id(), "");
            if (!pattern1.matcher(command).find() && !pattern2.matcher(command).find()
                && !pattern3.matcher(command).find() && !pattern4.matcher(command).find()
                && !pattern5.matcher(command).find()) {
                text = "Команда не распознана."
                    + "Введите /help, чтобы ознакомиться с допустимыми командами.";
                log.info(text);
                res = new SendMessage(update.message().chat().id(), text);
            } else if (pattern2.matcher(command).find()) {
                text = "Вы уже зарегистрированы";
                log.info(text);
                res = new SendMessage(update.message().chat().id(), text);
            } else {
                if (pattern1.matcher(command).find()) {
                    var str = list();
                    log.info(str);
                    res = new SendMessage(update.message().chat().id(), str);
                } else {
                    if (pattern3.matcher(command).find()) {
                        var answer = "Введите ссылку на ресурс, который хотите отслеживать";
                        condition = 1;
                        log.info(answer);
                        res = new SendMessage(update.message().chat().id(), answer);
                    } else {
                        if (pattern4.matcher(command).find()) {
                            condition = 2;
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
            return res;
        }
        if (condition == 1) {
            var result = track(command);
            condition = 0;
            log.info(result);
            return new SendMessage(id, result);
        }
        condition = 0;
        var t = untrack(command);
        log.info(t);
        return new SendMessage(id, t);
    }

    public static String list() {
        if (LINKSSET.isEmpty()) {
            return "Список отслеживаемых ресурсов пуст";
        } else {
            StringBuilder start = new StringBuilder("Текущий список отслеживаемых ресурсов:\n");
            for (String link : LINKSSET) {
                start.append(link + "\n");
            }
            return start.toString();
        }
    }

    public static String track(String link) {
        try {
            var url = new URI(link).toURL();
            if (!LINKSSET.contains(link)) {
                LINKSSET.add(link);
                return ("Ресурс добавлен");
            } else {
                return "Ресурс уже находится в списке отслеживаемых";
            }
        } catch (MalformedURLException | IllegalArgumentException | URISyntaxException e) {
            return ("Не удалось подключиться к заданному ресурсу."
                + "Проверьте корректность ссылки.");
        }
    }

    public static String untrack(String link) {
        if (LINKSSET.contains(link)) {
            LINKSSET.remove(link);
            return ("Ресурс удален");
        } else {
            return "Ресурс ранее вами не отслеживался.";
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
