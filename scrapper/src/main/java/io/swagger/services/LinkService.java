package io.swagger.services;

import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Collection<URI> add(long tgChatId, URI url);
    Collection<URI> remove(long tgChatId, URI url) ;

    Collection<URI> listAll(long tgChatId);
}
