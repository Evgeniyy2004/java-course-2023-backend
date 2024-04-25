package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository repo;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository repo) {
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
