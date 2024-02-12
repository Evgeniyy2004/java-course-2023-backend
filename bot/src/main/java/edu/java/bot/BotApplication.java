package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@Log
public class BotApplication {
    static ConcurrentHashMap<String, Set<String>> allForAll = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }


    public static void start(String id){
        if (allForAll.containsKey(id)) log.info("Вы уже зарегистрированы");
        allForAll.put(id, Collections.synchronizedSet(new HashSet<>()));
    }

    public static void command(String command, String id){
        Pattern pattern1 = Pattern.compile("(\\/list)( +)");
        if (pattern1.matcher(command).find()) {
            list(id);
        }
        Pattern pattern2 = Pattern.compile("(\\/start)( +)");
        if (pattern2.matcher(command).find()) {
            start(id);
        }
        Pattern pattern3 = Pattern.compile("(/track)( +)");
        if (pattern3.matcher(command).find()) {
            track(id);
        }
        Pattern pattern4 = Pattern.compile("(/untrack)( +)");
        if (pattern4.matcher(command).find()) {
            untrack(id);
        }
        Pattern pattern5 = Pattern.compile("(/help)( +)");
        if (pattern5.matcher(command).find()) {
            help();
        }
        log.info("Команда не распознана." +
            "Введите /help, чтобы ознакомиться с допустимыми командами.");
    }

    public static void list(String id){
        if (!allForAll.containsKey(id)) log.info("Зарегиструйтесь с помощью команды /start");
        else {
            for (String link: allForAll.get(id)) {
                log.info(link);
            }
        }
    }

    public static void track(String id){
        if (!allForAll.containsKey(id)) log.info("Зарегиструйтесь с помощью команды /start");
        else {
            log.info("Введите ссылку, которую хотите начать отслеживать");
            var link = System.console().readLine();
            try {
                Paths.get(link).toUri().toURL();
                if (!allForAll.get(id).contains(link)){
                    log.info("Ресурс добавлен");
                }
                allForAll.get(id).add(link);

            } catch (MalformedURLException | IllegalArgumentException e){
                log.info("Не удалось подключиться к заданному ресурсу." +
                    "Проверьте корректность ссылки.");
            }
        }
    }

    public static void untrack(String id){
        if (!allForAll.containsKey(id)) log.info("Зарегиструйтесь с помощью команды /start");
        else {
            log.info("Ваш текущий список отслеживаемых ресурсов:");
            StringBuilder res = new StringBuilder();
            for (String link: allForAll.get(id)) {
                res.append(link).append("\n");
            }
            log.info(res.toString());
            log.info("Введите ресурс, который хотите перестать отслеживать");
            var link = System.console().readLine();
            try {
                Paths.get(link).toUri().toURL();
                if (allForAll.get(id).contains(link)){
                    log.info("Ресурс удален");
                }
                allForAll.get(id).remove(link);

            } catch (MalformedURLException | IllegalArgumentException e){
                log.info("Не удалось подключиться к заданному ресурсу." +
                    "Проверьте корректность ссылки.");
            }
        }
    }

    public static void help(){
        StringBuilder res = new StringBuilder();
        res.append("/start - регистрация в боте\n");
        res.append("/help - список доступных команд\n");
        res.append("/track - добавление ресурса в отслеживаемые\n");
        res.append("/untrack - прекращение отслеживания ресурса\n");
        res.append("/list - список отслеживаемых ресурсов\n");
    }
}
