package io.swagger.services;

import io.swagger.api.ApiException;

public interface TgChatService {
    void register(long tgChatId) throws ApiException;

    void unregister(long tgChatId) throws ApiException;
}
