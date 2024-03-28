import edu.java.ScrapperApplication;
import edu.java.model.ApiException;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JdbcTgChatRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest(classes = ScrapperApplication.class)
@EnableTransactionManagement
public class JdbcLinkTest {


    @Autowired
    private JdbcTgChatRepository chatRepository;


    @Autowired
    private JdbcLinkRepository linkRepository;


    @Test
    @Transactional
    @DirtiesContext
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
    @DirtiesContext
    @Rollback
    void removeTest() {
        try {
            chatRepository.save(1L);
            chatRepository.remove(1L);
            chatRepository.save(1L);
            linkRepository.save(1L,"https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.remove(1L,"https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
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
            linkRepository.save(1L,"https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.save(1L,"https://github.com/krahets/hello-algo");
            linkRepository.findAll(1L);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @DirtiesContext
    @Rollback
    void failedRemoveTest() {
        try {
            chatRepository.save(1L);
            linkRepository.save(1L,"https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.remove(1L,"https://github.com/krahets/hello-algo");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
