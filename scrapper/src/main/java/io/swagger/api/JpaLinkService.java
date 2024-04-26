package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Collection;

public class JpaLinkService implements LinkService {

    private JpaLinkRepository repo;
    private JpaChatRepository repo1;

    public JpaLinkService(JpaLinkRepository repo, JpaChatRepository repo1) {
        this.repo1 = repo1;
        this.repo = repo;
    }

    @Override
    public void add(long tgChatId, String url, Timestamp time) throws ApiException {
        var check = repo.
        repo.save(tgChatId, url.toString(),time);
    }

    @Override
    public void remove(long tgChatId, String url) throws ApiException {
        repo.remove(tgChatId, url.toString());
    }

    @Override
    public Collection<URI> listAll(long tgChatId) throws ApiException {
        return repo.findAll(tgChatId);
    }
}
