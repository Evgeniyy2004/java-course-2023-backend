package io.swagger.api;

import edu.java.model.ApiException;
import edu.java.model.LinkResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Table(name="id")
@Repository
public interface JpaChatRepository extends JpaRepository<LinkResponse,Long> {

    @Override
    boolean existsById(@NotNull Long id);

    @Query(value = "insert into id (id) values (:id)", nativeQuery = true)
    void save(Long id);


    @Query(value = "delete from id where id=:id", nativeQuery = true)
    void remove(Long id);
}
