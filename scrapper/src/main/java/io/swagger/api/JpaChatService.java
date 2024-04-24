package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.TgChatService;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaChatService implements TgChatService {


    private JpaChatRepository repo;

    public JpaChatService(JpaChatRepository repo){
        this.repo = repo;
    }

    @Override
    public void register(long tgChatId) throws ApiException {
        repo.save(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) throws ApiException {
        repo.remove(tgChatId);
    }
}
