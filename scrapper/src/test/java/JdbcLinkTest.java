import edu.java.configuration.Configuration;
import io.swagger.api.ApiException;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JdbcTgChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {Configuration.class, JdbcLinkRepository.class, JdbcTgChatRepository.class})
@Testcontainers
public class JdbcLinkTest {

    @Autowired
    private JdbcTgChatRepository chatRepository;



    @Autowired
    private JdbcLinkRepository linkRepository;



    @Test
    @Transactional(transactionManager = "hibernateTransactionManager")
    @Rollback
    void addTest() {
        try {
            chatRepository.save(1L);
            chatRepository.save(2L);
            linkRepository.save(1L,"https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
    }
}
