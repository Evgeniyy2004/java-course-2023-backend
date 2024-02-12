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
    @DisplayName("При команде /list выводится список ресурсов, которые уже отслеживаются данным пользователем," +
        " если пользователь не зарегистрирован, выводится соответствующее сообщение")
    public void list() {
        var id = UUID.randomUUID().toString();
        BotApplication.command("  /start  ",id);
        BotApplication.command("  /list     ",id);
        var id1 = UUID.randomUUID().toString();
        BotApplication.command("  /list     ",id1);
    }


}
