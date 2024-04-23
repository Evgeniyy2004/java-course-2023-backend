package io.swagger.api;

import edu.java.model.ApiException;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Table(name="id")
@Table
@Repository
public class JpaLinkRepository implements LinkRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private GitHubClient git;

    @Autowired
    private StackOverflowClient stack;

    public void save(Long id, String link) throws ApiException {
        var q = manager.createQuery("SELECT distinct FROM id WHERE id=:id");
        if (first.length == 0) {
            throw new ApiException(404, "Чат не существует");
        }
        var time = new Timestamp(System.currentTimeMillis());
        var check = jdbcTemplate.queryForList("select id from connect where link=? and id=?", link, id).toArray();
        if (check.length > 0) {
            throw new ApiException(400, "Ссылка уже добавлена");
        }
        var action = "insert into connect  values (?, ?, ?)";
        jdbcTemplate.update(action, link, id, time);

    }

    public void remove(Long id, String link) throws ApiException {

    }

    public Collection<URI> findAll(Long id) throws ApiException {
        return null;
    }

    public HashMap<Long, Collection<String>> update() {
        return null;
    }
}
