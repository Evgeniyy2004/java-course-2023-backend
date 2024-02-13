package edu.java.bot;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.UUID;

public class FirstTest {

    @DisplayName("При вводе несуществующей команды бот выводит соответствующее сообщение")
    @Test
    public void strangeCommand() {
        var id = UUID.randomUUID().toString();
        BotApplication.command("  /start  ",id);
        BotApplication.command("abcde",id);
    }

    @DisplayName("При команде /start от пользователя, который уже зарегистрирован в системе, бот выводит соответствующее сообщение")
    @Test
    public void alreadyExists() {
        var id = UUID.randomUUID().toString();
        BotApplication.command("  /start  ",id);
        BotApplication.command("/start",id);
    }


    @Test
    @DisplayName("При команде /help выводится список допустимых комманд")
    public void help() {
        var id = UUID.randomUUID().toString();
        BotApplication.command("  /start  ",id);
        BotApplication.command("  /help     ",id);
    }

    @Test
    @DisplayName("Если никакие ресурсы не отслеживаются, при команде /list выводится специальное сообщение")
    public void emptyList() {
        var id = UUID.randomUUID().toString();
        BotApplication.command("  /start  ",id);
        BotApplication.command("  /list     ",id);
    }


}
