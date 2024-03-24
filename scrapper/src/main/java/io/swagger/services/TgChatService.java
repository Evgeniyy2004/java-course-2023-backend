package io.swagger.services;

import edu.java.model.ApiException;

public interface TgChatService {
    void register(long tgChatId) throws ApiException;

    void unregister(long tgChatId) throws ApiException;
}
