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

    @Query(value = "insert into connect (id,link,updated) values (:id,:link,:time)", nativeQuery = true)
    void save(Long id, String link, Timestamp time);

    @Query(value = "update connect set updated=:time where id=:id and link =: link")
    void update (Long id, String link, Timestamp time);

    @Query("select count(e)>0 from connect e where e.id=:id and e.link=:link")
    boolean existsByIdAndUrl(Long id, String link);

    @Query("select u from  connect u where u.id=:id")
    List<LinkResponse> findAllById(Long id) ;


    @Query("delete from  connect  where id=:id and link=:link")
    void delete(Long id, String link);


    @Query("select u from connect where updated<time")
    List<LinkResponse> findByTime(Timestamp time);
}
