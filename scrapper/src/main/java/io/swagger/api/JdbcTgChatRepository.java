package io.swagger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MagicNumber")
public class JdbcTgChatRepository {
    private static final String QUERY = "select * from id where id=?";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTgChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long id) throws ApiException {

        String query = (QUERY);
        var res = jdbcTemplate.queryForList(query, id).toArray();
        if (res.length != 0) {
            throw new ApiException(409, "Вы не можете повторно зарегистрировать чат");
        }
        jdbcTemplate.execute("insert into id (id) values " + "(" + id + ")");
    }

    public void remove(Long id) throws ApiException {
        String query = (QUERY);
        var res = jdbcTemplate.queryForList(query, id).toArray();
        if (res.length == 0) {
            throw new ApiException(404, "Чат не существует");
        }
        jdbcTemplate.execute("delete from id where id=" + id);
    }

}
