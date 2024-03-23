package io.swagger.api;

import io.swagger.services.LinkService;
import java.net.URI;
import java.util.Collection;
import model.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {

    @Autowired
    private JdbcLinkRepository repo;

    @Override
    public void add(long tgChatId, URI url) throws ApiException {
        repo.save(tgChatId, url.toString());
    }

    @Override
    public void remove(long tgChatId, URI url) throws ApiException {
        repo.remove(tgChatId, url.toString());
    }

    @Override
    public Collection<URI> listAll(long tgChatId) throws ApiException {
        return repo.findAll(tgChatId);
    }
}
