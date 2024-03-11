package io.swagger.services;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TgChatService {
    Optional<Long> register(long tgChatId);
    Optional<Long> unregister(long tgChatId);
}
