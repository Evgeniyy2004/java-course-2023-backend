package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.util.Collection;

public class JpaLinkService implements LinkService {

    private JpaLinkRepository repo;

    public JpaLinkService(JpaLinkRepository repo) {
        this.repo = repo;
    }

    @Override
    public void add(long tgChatId, String url) throws ApiException {
        repo.save(tgChatId, url.toString());
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
