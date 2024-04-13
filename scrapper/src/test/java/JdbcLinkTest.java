import edu.java.model.ApiException;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JdbcTgChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@EnableTransactionManagement
public class JdbcLinkTest extends IntegrationTest {

    DriverManagerDataSource data =
        new DriverManagerDataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

    private final JdbcTgChatRepository chatRepository = new JdbcTgChatRepository(new JdbcTemplate(data));

    private final JdbcLinkRepository linkRepository = new JdbcLinkRepository(new JdbcTemplate(data));

    @Test
    @Transactional
    @DirtiesContext
    @Rollback
    void addTest() {
        try {
            chatRepository.save(1L);
            chatRepository.save(2L);
            linkRepository.save(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            chatRepository.remove(1L);
            chatRepository.remove(2L);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @DirtiesContext
    @Rollback
    void findAllTest() {
        try {
            chatRepository.save(1L);
            linkRepository.save(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.save(1L, "https://github.com/krahets/hello-algo");
            linkRepository.findAll(1L);
            chatRepository.remove(1L);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @DirtiesContext
    @Rollback
    void failedRemoveTest() throws ApiException {
        try {
            chatRepository.save(1L);
            linkRepository.save(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.remove(1L, "https://github.com/krahets/hello-algo");
        } catch (ApiException e) {
            e.printStackTrace();
            chatRepository.remove(1L);
        }
    }

    @Test
    @Transactional
    @DirtiesContext
    @Rollback
    void removeTest() {
        try {
            chatRepository.save(1L);
            chatRepository.remove(1L);
            linkRepository.save(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
