package io.swagger.services;

import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url);
    Collection<URI> listAll(long tgChatId);
}
