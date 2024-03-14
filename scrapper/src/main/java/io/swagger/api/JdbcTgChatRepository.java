package io.swagger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class JdbcTgChatRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTgChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    void save(Long id) throws ApiException {
        try {
            String query = ("select * from id where id=") + id.toString();
            var res = jdbcTemplate.queryForObject(query, Long.class);
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.execute("insert into id (id) values "+"("+id+")");
            return;
        }
        throw new ApiException(404,"Чат уже зарегистрирован");
    }

    void remove(Long id)throws ApiException {
        try {
            String query = ("select * from id where id=") + id.toString();
            jdbcTemplate.queryForObject(query, Long.class);
            jdbcTemplate.execute("delete from id where id="+ id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException(404,"Чат не существует");
        }

    }




}
