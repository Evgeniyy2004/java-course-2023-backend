package io.swagger.services;

import io.swagger.api.ApiException;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    public void add(long tgChatId, URI url) throws ApiException;
    void remove(long tgChatId, URI url) throws ApiException;

    Collection<URI> listAll(long tgChatId) throws  ApiException;
}
