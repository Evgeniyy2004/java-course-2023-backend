package io.swagger.services;

import java.net.URI;
import java.util.Collection;

public interface LinkService {
    void add(long tgChatId, URI url);
    void remove(long tgChatId, URI url);

    Collection<URI> listAll(long tgChatId);
}
