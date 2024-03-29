package io.swagger.services;

import edu.java.model.ApiException;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    void add(long tgChatId, String url) throws ApiException;

    void remove(long tgChatId, String url) throws ApiException;

    Collection<URI> listAll(long tgChatId) throws ApiException;
}
