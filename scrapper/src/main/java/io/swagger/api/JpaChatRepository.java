package io.swagger.api;

import edu.java.model.ApiException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaChatRepository {

    private EntityManager manager;

    private static final int CONFLICT = 409;

    private static final int NOTFOUND = 404;

    private static final String QUERY = "select * from id where id=?";

    public JpaChatRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    public void save(Long id) throws ApiException {
        var act = manager.createNativeQuery(QUERY);
        act.setParameter(1, id);
        var res = act.getResultList();
        if (res.size() != 0) {
            throw new ApiException(CONFLICT, "Вы не можете повторно зарегистрировать чат");
        }
        var insert = manager.createNativeQuery("insert into id (id) values " + "(" + id + ")");
        manager.joinTransaction();
        insert.executeUpdate();
    }

    @Transactional
    public void remove(Long id) throws ApiException {
        var act = manager.createNativeQuery(QUERY);
        act.setParameter(1, id);
        var res = act.getResultList();
        if (res.isEmpty()) {
            throw new ApiException(NOTFOUND, "Чат не существует");
        }
        var insert = manager.createNativeQuery("delete from id where id=" + id);
        insert.executeUpdate();
    }
}
