package io.swagger.api;

import edu.java.model.ApiException;
import io.swagger.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

public class JpaLinkService implements LinkService {

    private JpaLinkRepository repo;
    private JpaChatRepository repo1;

    public JpaLinkService(JpaLinkRepository repo, JpaChatRepository repo1) {
        this.repo1 = repo1;
        this.repo = repo;
    }

    @Override
    public void add(long tgChatId, String url, Timestamp time) throws ApiException {
        var check = repo1.existsById(tgChatId);
        if (!check){
            throw  new ApiException(404,"Чат не существует");
        } repo.save(tgChatId, url.toString(),time);
    }

    @Override
    public void remove(long tgChatId, String url) throws ApiException {
        var check = repo.existsByIdAndUrl(tgChatId,  url);
        if (!check){
            throw  new ApiException(409,"Ссылка не отслеживается");
        } repo.remove(tgChatId, url.toString());
    }

    @Override
    public Collection<URI> listAll(long tgChatId) throws ApiException {
        var check = repo1.existsById(tgChatId);
        if (!check){
            throw  new ApiException(404,"Чат не существует");
        }
        var res =  repo.findAllById(tgChatId);
        URI[] result = new URI[res.size()];
        for(int u=0; u < res.size();u++){
            try {
                result[u] = new URI(res.get(u).getUrl());
            } catch (URISyntaxException e){
                throw  new ApiException(500,"Ошибка на сервере, повторите попытку позднее");
            }
        }
        return Arrays.asList(result);
    }
}
