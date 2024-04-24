import edu.java.model.ApiException;
import io.swagger.api.JpaChatRepository;
import io.swagger.api.JpaLinkRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@EnableTransactionManagement
public class JpaLinkTest extends IntegrationTest {


    private final JpaChatRepository chatRepository = new JpaChatRepository(em);

    private final JpaLinkRepository linkRepository = new JpaLinkRepository(em);

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
    void failedRemoveTest() {
        try {
            chatRepository.save(1L);
            linkRepository.save(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java");
            linkRepository.remove(1L, "https://github.com/krahets/hello-algo");
            chatRepository.remove(1L);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
