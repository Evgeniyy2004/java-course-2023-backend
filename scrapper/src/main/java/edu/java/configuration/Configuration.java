package edu.java.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.botclient.UpdatesClient;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.swagger.api.JdbcLinkService;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
@SpringBootConfiguration
@ComponentScan
public class Configuration {

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
        WebClient restClient = WebClient.builder().baseUrl("http://localhost:8080/").build();
        WebClientAdapter adapter = WebClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UpdatesClient.class);
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5437/scrapper");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }




    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource d) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(d);
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }


    @Bean
    public JdbcTemplate template(DataSource d) {
        return new JdbcTemplate(d);
    }



}

