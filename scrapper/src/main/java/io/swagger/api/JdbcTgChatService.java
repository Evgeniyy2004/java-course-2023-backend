package io.swagger.api;

import io.swagger.services.TgChatService;
import edu.java.model.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService implements TgChatService {

    @Autowired
    private JdbcTgChatRepository repo;

    @Override
    public void register(long tgChatId) throws ApiException {
        repo.save(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) throws ApiException {
        repo.remove(tgChatId);
    }
}
