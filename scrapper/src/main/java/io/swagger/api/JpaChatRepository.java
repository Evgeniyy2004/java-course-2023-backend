package io.swagger.api;

import edu.java.model.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JpaChatRepository{
    @PersistenceContext
    private EntityManager manager;

    private static final String QUERY = "select * from id where id=?";


    public void save(Long id) throws ApiException {
        String query = (QUERY);
        var act = manager.createNativeQuery(QUERY);
        act.setParameter(1,id);
         var res =   act.getResultList();
        if (res.size() != 0) {
            throw new ApiException(409, "Вы не можете повторно зарегистрировать чат");
        }
        var insert = manager.createNativeQuery("insert into id (id) values " + "(" + id + ")");
        insert.executeUpdate();
    }

    public void remove(Long id) throws ApiException {
        var act = manager.createNativeQuery(QUERY);
        act.setParameter(1,id);
        var res = act.getResultList();
        if (res.isEmpty()) {
            throw new ApiException(404, "Чат не существует");
        }
        var insert = manager.createNativeQuery("delete from id where id=" + id);
        insert.executeUpdate();
    }
}
