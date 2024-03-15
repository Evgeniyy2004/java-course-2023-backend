package io.swagger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long id, String link) throws ApiException {
        try {
            String query = ("select * from id where id=") + id.toString();
            jdbcTemplate.queryForObject(query, Long.class);
            var time = new Timestamp(System.currentTimeMillis());
            MapSqlParameterSource params = new MapSqlParameterSource();

            params.addValue("id",id);
            params.addValue("link",link);
            params.addValue("updated",time);
            var action ="insert into connect  values (?, ?, ?)";
            jdbcTemplate.update(action, link,id,time);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException(404,"Чат не существует");
        }
    }

    public void remove(Long id, String link)throws ApiException {
        try {
            String query = ("select * from id where id=") + id.toString();
            jdbcTemplate.queryForObject(query, Long.class);
            jdbcTemplate.update("delete from connect where link =? and id = ?", link,id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException(404,"Чат не существует");
        }
    }

    public Collection<URI> findAll(Long id)throws ApiException {
        try {
            String query = ("select * from id where id=") + id.toString();
            jdbcTemplate.queryForObject(query, Long.class);
            ResultSet res = jdbcTemplate.queryForObject("select link from connect where id="+id+";",ResultSet.class);
            ArrayList<URI> result = new ArrayList<>();
            if (res != null && res.last()) {
                result = new ArrayList<>(res.getRow());
                int i =0;
                while (res.next()) {
                    result.add(i,new URI(res.getObject("link",String.class)));
                    i++;
                }
            }
            return result;
        } catch (EmptyResultDataAccessException | SQLException e) {
            throw new ApiException(404,"Чат не существует");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
