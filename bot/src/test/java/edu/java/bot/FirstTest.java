package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Bot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class FirstTest {

    @DisplayName("При вводе несуществующей команды бот выводит соответствующее сообщение")
    @Test
    public void strangeCommand() {
        var botExample = new Bot(new ApplicationConfig("test"));
        Update update = Mockito.mock(Update.class);
        when(update.message()).thenReturn(Mockito.mock(Message.class));
        when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn(" /start");
        botExample.handle(update);
        when(update.message().text()).thenReturn(" /do");
        botExample.handle(update);
    }

    @DisplayName(
        "При команде /start от пользователя, который уже зарегистрирован в системе, бот выводит соответствующее сообщение")
    @Test
    public void alreadyExists() {
        var botExample = new Bot(new ApplicationConfig("test"));
        Update update = Mockito.mock(Update.class);
        when(update.message()).thenReturn(Mockito.mock(Message.class));
        when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        when(update.message().chat().id()).thenReturn(78787L);
        when(update.message().text()).thenReturn(" /start");
        botExample.handle(update);
        when(update.message().text()).thenReturn("  /start   ");
        botExample.handle(update);
    }

    @Test
    @DisplayName("При команде /help выводится список допустимых комманд")
    public void help() {
        var botExample = new Bot(new ApplicationConfig("test"));
        Update update = Mockito.mock(Update.class);
        when(update.message()).thenReturn(Mockito.mock(Message.class));
        when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        when(update.message().chat().id()).thenReturn(55676L);
        when(update.message().text()).thenReturn(" /help    ");
        botExample.handle(update);
    }

    @Test
    @DisplayName("Если никакие ресурсы не отслеживаются, при команде /list выводится специальное сообщение")
    public void emptyList() {
        var botExample = new Bot(new ApplicationConfig("test"));
        Update update = Mockito.mock(Update.class);
        when(update.message()).thenReturn(Mockito.mock(Message.class));
        when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        when(update.message().chat().id()).thenReturn(90L);
        when(update.message().text()).thenReturn(" /start");
        botExample.handle(update);
        when(update.message().text()).thenReturn("  /list   ");
        botExample.handle(update);
    }

    @Test
    @DisplayName("При добавлении ссылки она появится в списке отслеживаемых ресурсов")
    public void addToList() {
        var botExample = new Bot(new ApplicationConfig("test"));
        Update update = Mockito.mock(Update.class);
        when(update.message()).thenReturn(Mockito.mock(Message.class));
        when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        when(update.message().chat().id()).thenReturn(90L);
        when(update.message().text()).thenReturn(" /start");
        botExample.handle(update);
        when(update.message().text()).thenReturn("  /track   ");
        botExample.handle(update);
        when(update.message().text()).thenReturn("https://stackoverflow.com/");
        botExample.handle(update);
        when(update.message().text()).thenReturn("  /list   ");
        botExample.handle(update);

    }
}
