package io.swagger.api;

import edu.java.configuration.Configuration;
import io.swagger.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.Collection;

@Import(JdbcLinkRepository.class)
@Service
public class JdbcLinkService implements LinkService {

    @Autowired
    private JdbcLinkRepository repo;
    @Override
    public void add(long tgChatId, URI url) throws ApiException {
            repo.save(tgChatId,url.toString());
    }

    @Override
    public void remove(long tgChatId, URI url) throws ApiException{
        repo.remove(tgChatId,url.toString());
    }

    @Override
    public Collection<URI> listAll(long tgChatId) throws ApiException{
       return repo.findAll(tgChatId);
    }
}
