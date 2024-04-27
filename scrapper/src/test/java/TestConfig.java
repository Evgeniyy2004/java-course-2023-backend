import io.swagger.api.JpaChatRepository;
import io.swagger.api.JpaChatService;
import io.swagger.api.JpaLinkRepository;
import io.swagger.api.JpaLinkService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@TestConfiguration
@EntityScan("edu.java.model")
@EnableJpaRepositories(basePackages = {"io.swagger.api"})
public class TestConfig {
    @Bean
    public JpaLinkService linkService(JpaLinkRepository repo, JpaChatRepository repo1) {
        return new JpaLinkService(repo, repo1);
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(IntegrationTest.data);
        sessionFactory.setHibernateProperties(IntegrationTest.PROPERTIES);
        return sessionFactory;
    }

    @Bean
    public JpaChatService chatService(JpaChatRepository repo) {
        return new JpaChatService(repo);
    }
}
