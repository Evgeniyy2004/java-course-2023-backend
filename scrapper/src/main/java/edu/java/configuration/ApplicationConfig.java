package edu.java.configuration;

import edu.java.botclient.UpdatesClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JdbcTgChatRepository;
import java.util.Properties;
import javax.sql.DataSource;
import io.swagger.api.JpaChatRepository;
import io.swagger.api.JpaChatService;
import io.swagger.api.JpaLinkRepository;
import io.swagger.api.JpaLinkService;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class ApplicationConfig {

    private static final String BASE = "http://localhost:8081/";

    @Bean
    public JdbcTgChatRepository chatrepo() {
        return new JdbcTgChatRepository(template());
    }

    @Bean
    public JdbcLinkRepository linkrepo() {
        return new JdbcLinkRepository(template());
    }

    @Bean
    public StackOverflowClient beanStack() {
        WebClient restClient = WebClient.builder().baseUrl("https://api.stackexchange.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public GitHubClient beanGit() {
        WebClient restClient = WebClient.builder().baseUrl("https://api.github.com/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public UpdatesClient beanUpdates() {
        WebClient restClient = WebClient.builder().baseUrl(BASE).build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UpdatesClient.class);
    }

    @Bean
    public DataSource dataSource() {
        var namePassword = "postgres";
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5433/scrapper");
        dataSource.setUsername(namePassword);
        dataSource.setPassword(namePassword);
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject());
        return txManager;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
            "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return hibernateProperties;
    }

    @Bean
    public JpaChatRepository jpaChat() {
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        return new JpaChatRepository(em);
    }

    @Bean
    public JpaChatService jpaChatService(JpaChatRepository repo){
        return new JpaChatService(repo);
    }

    @Bean
    public JpaLinkRepository jpaLink() {
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        return new JpaLinkRepository(em,beanGit(),beanStack());
    }

    @Bean
    public JpaLinkService jpaChatService(JpaLinkRepository repo){
        return new JpaLinkService(repo);
    }

    @Bean
    public JdbcTemplate template() {
        return new JdbcTemplate(dataSource());
    }
}
