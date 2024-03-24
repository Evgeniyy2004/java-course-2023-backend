package io.swagger.api;

import edu.java.model.ApiException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository {
    void save(Long id, String link) throws ApiException;

    void remove(Long id, String link) throws ApiException;

    Collection<URI> findAll(Long id) throws ApiException;

    HashMap<Long, Collection<String>> update();
}
