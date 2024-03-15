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
import java.util.List;

@Repository
public class JdbcLinkRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long id, String link) throws ApiException {
        try {
            String query = ("select * from id where id=?");
            var first = jdbcTemplate.queryForList(query,id).toArray();
            if (first.length == 0) throw new ApiException(404, "Чат не существует");
            var time = new Timestamp(System.currentTimeMillis());
            MapSqlParameterSource params = new MapSqlParameterSource();
            var check = jdbcTemplate.queryForList("select id from connect where link=? and id=?", link,id).toArray();
            if (check.length > 0 ) throw new ApiException(400,"Ссылка уже добавлена");
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
            String query = ("select * from id where id=?");
            var doo = jdbcTemplate.queryForList(query, id).toArray();
            if (doo.length == 0) throw new ApiException(404,"Чат не существует");
            var check = jdbcTemplate.queryForList("select id from connect where link=? and id=?", link,id).toArray();
            if (check.length == 0 ) throw new ApiException(400,"Ссылка не отслеживается");
            jdbcTemplate.update("delete from connect where link =? and id = ?", link,id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException(404,"Чат не существует");
        }
    }

    public Collection<URI> findAll(Long id) throws ApiException {
        try {
            String query = ("select * from id where id=?");
            var doo = jdbcTemplate.queryForList(query, id).toArray();
            if (doo.length == 0) throw new ApiException(404,"Чат не существует");
            var res = jdbcTemplate.queryForList("select link from connect where id="+id+";",String.class).toArray();
            var result = new URI[res.length];
            for (int i =0; i < res.length;i++) {
                result[i]=new URI(res[i].toString());
            }
            return List.of(result);
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException(404,"Чат не существует");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
