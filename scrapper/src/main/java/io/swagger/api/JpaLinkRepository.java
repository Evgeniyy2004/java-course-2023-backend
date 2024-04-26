package io.swagger.api;

import edu.java.model.ApiException;
import edu.java.model.LinkResponse;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Table(name = "id")
@SecondaryTable(name = "connect")
@SuppressWarnings("all")
public interface JpaLinkRepository extends CrudRepository<LinkResponse,Long> {


    public void remove(Long id, String link) throws ApiException {
        var q = manager.createNativeQuery("select * FROM id WHERE id=?");
        q.setParameter(1, id);
        var first = q.getResultList();
        if (first.isEmpty()) {
            throw new ApiException(404, "Чат не существует");
        }
        var check = manager.createNativeQuery("select * from connect where link=? and id=?");
        check.setParameter(2, id);
        check.setParameter(1, link);
        var two = check.getResultList();
        if (two.isEmpty()) {
            throw new ApiException(409, "Ссылка не отслеживается");
        }
        var action = manager.createNativeQuery("delete from  connect  where id =? and link=?");
        action.setParameter(1, id);
        action.setParameter(2, link);
        manager.getTransaction().begin();
        action.executeUpdate();
        manager.getTransaction().commit();
    }

    public Collection<URI> findAll(Long id) throws ApiException {
        try {
            String query = ("select * from id where id=?");
            var act = manager.createNativeQuery(query);
            act.setParameter(1, id);
            var doo = act.getResultList();
            if (doo.isEmpty()) {
                throw new ApiException(404, "Чат не существует");
            }
            var another =
                manager.createNativeQuery("select link from connect where id=" + id);
            var res = another.getResultList();
            var result = new URI[res.size()];
            for (int i = 0; i < res.size(); i++) {
                result[i] = new URI(res.get(i).toString());
            }
            return List.of(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Long, Collection<String>> update() {
        var time = new Timestamp(System.currentTimeMillis() - 3600000);
        var now = new Timestamp(System.currentTimeMillis());
        var query =
            manager.createNativeQuery("select (id,link,updated) from connect where updated<" + time, ArrayList.class);
        var query1 = manager.createNativeQuery("update connect set updated=? where update<?");
        query1.setParameter(1, time);
        query1.setParameter(2, now);
        manager.getTransaction().begin();
        query1.executeUpdate();
        manager.getTransaction().commit();
        var res = query.getResultList();
        HashMap<Long, Collection<String>> result = new HashMap<>();
        for (Object i : res) {
            var list = (ArrayList) i;
            var current = list.get(1).toString();
            var id = Long.parseLong(list.get(0).toString());
            if (current.startsWith("https://stackoverflow.com/questions/")) {
                current = current.replace("https://stackoverflow.com/questions/", "");
                var question = Long.parseLong(current.split("/")[0]);
                var response = stack.fetchQuestion(question);
                if (Timestamp.valueOf(response.time.toLocalDateTime()).after((Timestamp) list.get(2))) {
                    if (!result.containsKey(id)) {
                        result.put(id, new ArrayList<>());
                    }
                    result.get(id).add(list.get(1).toString());
                }
            } else {
                current = current.replace("https://github.com/", "");
                var repoAuthor = current.split("/");
                var response = git.fetchRepository(repoAuthor[0], repoAuthor[1]);
                if (Timestamp.valueOf(response.time.toLocalDateTime()).after((Timestamp) list.get(2))) {
                    if (!result.containsKey(id)) {
                        result.put(id, new ArrayList<>());
                    }
                    result.get(id).add(list.get(1).toString());
                }
            }
        }
        return result;
    }


    @Query(value = "insert into connect (id,link,updated) values (:id,:link,:time)", nativeQuery = true)
    void save(Long id, String link, Timestamp time);

    @Query(value = "update connect set updated=:time where id=:id and link =: link")
    void update (Long id, String link, Timestamp time);



    @Query("select u from  connect u where u.id=:id")
    List<LinkResponse> findAllById(Long id) ;


    @Query("delete from  connect  where id=:id and link=:link")
    void delete(Long id, String link);


    @Query("select u from connect where updated<time")
    List<LinkResponse> findByTime(Timestamp time);
}
