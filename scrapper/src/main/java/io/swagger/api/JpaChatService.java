package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.TgChatService;
import org.springframework.beans.factory.annotation.Autowired;


public class JpaChatService implements TgChatService {


    private JpaChatRepository repo;

    public JpaChatService(JpaChatRepository repo) {
        this.repo = repo;
    }

    @Override
    public void register(long tgChatId) throws ApiException {
        var find = repo.existsById(tgChatId);
        if (find){
            throw new ApiException(409,"Вы не можете повторно зарегистрировать чат");
        } else  repo.save(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) throws ApiException {
        var find = repo.existsById(tgChatId);
        if (!find){
            throw new ApiException(404,"Чат не зарегистрирован");
        } else  repo.save(tgChatId);
    }
}
