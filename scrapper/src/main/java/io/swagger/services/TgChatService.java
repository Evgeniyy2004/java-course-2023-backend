package io.swagger.services;

import io.swagger.api.ApiException;
import javax.swing.text.html.Option;
import java.util.Optional;

public interface TgChatService {
    void register(long tgChatId) throws ApiException;
    void unregister(long tgChatId) throws ApiException ;
}
