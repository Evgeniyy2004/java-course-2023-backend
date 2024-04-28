package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcLinkService implements LinkService {
    private JdbcLinkRepository repo;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository repo) {
        this.repo = repo;
    }


    @Override
    public void add(long tgChatId, String url, Timestamp time) throws ApiException {
        repo.save(tgChatId, url.toString());
    }

    @Autowired
    public JdbcLinkService(JdbcLinkRepository repo){
        this.repo = repo;
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
