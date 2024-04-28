package listener;

import edu.java.botclient.UpdatesClient;
import edu.java.model.LinkResponse;
import edu.java.model.LinkUpdate;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JpaLinkRepository;
import java.sql.Timestamp;
import edu.java.configuration.ApplicationConfig;
import edu.java.model.LinkUpdate;
import io.swagger.api.LinkRepository;
import io.swagger.api.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "scheduler", ignoreUnknownFields = false)
@EnableScheduling
public class LinkUpdaterScheduler {

    public enum AccessType {
        JPA, JDBC
    }

    @Value("scheduler.use")
    private AccessType type;

    @Autowired
    private UpdatesClient client;

    @Autowired
    private JdbcLinkRepository repo;

    @Autowired
    private JpaLinkRepository repo1;

    @Autowired
    private GitHubClient git;

    @Autowired
    private StackOverflowClient stack;

    @Scheduled(fixedDelayString = "86400s")
    public void update() {
        var time = new Timestamp(System.currentTimeMillis() - 3600000);
        if (type == AccessType.JPA) {
            var res = repo1.findByTime(time);
            for (LinkResponse lr : res) {
                var request = new LinkUpdate();
                request.setId(lr.getId());
                request.setUrl(lr.getUrl());
                client.post(request);

            }
        } else {
            var result = repo.update();
            for (Long id : result.keySet()) {
                var request = new LinkUpdate();
                request.setId(id);
                for (String url : result.get(id)) {
                    request.setUrl(url);
                    client.post(request);
                }
            }
        }
    }
}
