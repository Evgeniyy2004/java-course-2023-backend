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
    public void save(Long id) throws ApiException {

            String query = ("select * from id where id=?");
            var res = jdbcTemplate.queryForList(query, id).toArray();
            if (res.length != 0) throw new ApiException(409,"Вы не можете повторно зарегистрировать чат");
            jdbcTemplate.execute("insert into id (id) values "+"("+id+")");
    }

    public void remove(Long id)throws ApiException {
            String query = ("select * from id where id=?");
            var res = jdbcTemplate.queryForList(query, id).toArray();
            if (res.length == 0) throw new ApiException(404,"Чат не существует");
            jdbcTemplate.execute("delete from id where id="+ id);
    }




}
