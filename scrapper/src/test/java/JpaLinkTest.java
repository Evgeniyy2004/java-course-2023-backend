import edu.java.ScrapperApplication;
import edu.java.model.ApiException;
import io.swagger.api.JpaChatRepository;
import io.swagger.api.JpaChatService;
import io.swagger.api.JpaLinkRepository;
import io.swagger.api.JpaLinkService;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Timestamp;

@Testcontainers
public class JpaLinkTest extends IntegrationTest {

    @TestConfiguration
    @EnableJpaRepositories({"io.swagger.api"})
    static class TestConfig {
        @Bean
        public JpaLinkService jpaLinkService(JpaLinkRepository repo, JpaChatRepository repo1) {
            return new JpaLinkService(repo, repo1);
        }

        @Bean
        public JpaChatService jpaChatService(JpaChatRepository repo) {
            return new JpaChatService(repo);
        }

    }
    @Autowired
    private JpaChatService chatService;

    @Autowired
    private JpaLinkService linkService;



    @org.junit.jupiter.api.Test
    @DirtiesContext
    public void addTest() {
        try {
            chatService.register(1L);
            chatService.register(2L);
            var time = new Timestamp(System.currentTimeMillis());
            linkService.add(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java",time);
            chatService.unregister(1L);
            chatService.unregister(2L);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    @DirtiesContext
    public void findAllTest() throws ApiException {
            chatService.register(1L);
            var time = new Timestamp(System.currentTimeMillis());
            linkService.add(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java",time);
            var other = new Timestamp(System.currentTimeMillis());
            linkService.add(1L, "https://github.com/krahets/hello-algo",other);
            linkService.listAll(1L);
            chatService.unregister(1L);

    }

    @org.junit.jupiter.api.Test
    @DirtiesContext
    public void failedRemoveTest() {
        try {
            chatService.register(1L);
            var time = new Timestamp(System.currentTimeMillis());
            linkService.add(1L,
                "https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java", time
            );
            linkService.remove(1L, "https://github.com/krahets/hello-algo");
            chatService.unregister(1L);
        } catch (ApiException e){
            e.printStackTrace();
        }
    }
}
