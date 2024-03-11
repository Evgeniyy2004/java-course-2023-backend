package io.swagger.services;

public interface TgChatService {
    void register(long tgChatId);
    void unregister(long tgChatId);
}
