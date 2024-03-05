import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.JdbcDatabaseContainer;
import java.sql.SQLException;

@SpringBootTest
public class MyFirstDBTest extends IntegrationTest{

    @Test
    public void run(){
    }
}
