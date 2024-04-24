import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Properties;
import jakarta.persistence.EntityManager;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;
    public static Properties PROPERTIES;

    static DriverManagerDataSource data;
    static HibernateTransactionManager MANAGER ;

    static   EntityManager em;
    public static LocalSessionFactoryBean FACTORY;
    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
            "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        PROPERTIES= hibernateProperties;
        POSTGRES.start();
        data = new DriverManagerDataSource(POSTGRES.getJdbcUrl(),POSTGRES.getUsername(),POSTGRES.getPassword());
        hibernateProperties.setProperty("jakarta.persistence.url",POSTGRES.getJdbcUrl());
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setHibernateProperties(hibernateProperties);
        sessionFactory.setDataSource(data);
        try {
            sessionFactory.afterPropertiesSet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FACTORY = sessionFactory;
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory.getObject());
        em = sessionFactory.getObject().createEntityManager();
        MANAGER = txManager;
        try {
            runMigrations(POSTGRES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) throws Exception {
        java.sql.Connection connection =
            DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        Database database =
            DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        var way = new File(new File(".").getAbsoluteFile().getParent()).toPath()
            .toAbsolutePath().getParent().resolve("scrapper").resolve("src").resolve("main").resolve("resources").resolve("migrations");
        Liquibase liquibase = new liquibase.Liquibase("master.xml", new DirectoryResourceAccessor(way), database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("jakarta.persistence.url",POSTGRES::getJdbcUrl);
    }
}
