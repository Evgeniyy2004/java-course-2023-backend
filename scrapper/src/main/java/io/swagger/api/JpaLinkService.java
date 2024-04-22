package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.util.Collection;



public class JpaLinkService implements LinkService {


    @Override
    public void add(long tgChatId, String url) throws ApiException {

    }

    @Override
    public void remove(long tgChatId, String url) throws ApiException {

    }

    @Override
    public Collection<URI> listAll(long tgChatId) throws ApiException {
        return null;
    }
}
