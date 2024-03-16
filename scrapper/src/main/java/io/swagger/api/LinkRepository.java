package io.swagger.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface LinkRepository {
    public void save(Long id, String link) throws ApiException ;


    public void remove(Long id, String link)throws ApiException;

    public Collection<URI> findAll(Long id) throws ApiException;


    public HashMap<Long,Collection<URI>> update();
}
