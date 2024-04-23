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
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Table(name="id")
@SecondaryTable(name="connect")
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
        q.setParameter("id",id);
        var first = q.getResultList();
        if (first.isEmpty()) {
            throw new ApiException(404, "Чат не существует");
        }
        var time = new Timestamp(System.currentTimeMillis());
        var check = manager.createQuery("select * from connect where link=:link and id=:id");
        check.setParameter("id",id);
        check.setParameter("link",link);
        var two = check.getResultList();
        if (!two.isEmpty()) {
            throw new ApiException(409, "Ссылка уже добавлена");
        }
        var action = manager.createQuery( "insert into connect  values (?, ?, ?)");
        action.setParameter(1,link);
        action.setParameter(2,id);
        action.setParameter(3,time);
        action.executeUpdate();

    }

    public void remove(Long id, String link) throws ApiException {
        var q = manager.createQuery("SELECT distinct FROM id WHERE id=:id");
        q.setParameter("id",id);
        var first = q.getResultList();
        if (first.isEmpty()) {
            throw new ApiException(404, "Чат не существует");
        }
        var check = manager.createQuery("select * from connect where link=:link and id=:id");
        check.setParameter("id",id);
        check.setParameter("link",link);
        var two = check.getResultList();
        if (two.isEmpty()) {
            throw new ApiException(409, "Ссылка не отслеживается");
        }
        var action = manager.createQuery( "delete from  connect  where id =? and link=?");
        action.setParameter(1,id);
        action.setParameter(2,link);
        action.executeUpdate();
    }

    public Collection<URI> findAll(Long id) throws ApiException {
        return null;
    }

    public HashMap<Long, Collection<String>> update() {
        return null;
    }
}
