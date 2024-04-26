package listener;

import edu.java.botclient.UpdatesClient;
import edu.java.model.LinkResponse;
import edu.java.model.LinkUpdate;
import edu.java.siteclients.GitHubClient;
import edu.java.siteclients.StackOverflowClient;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.JpaLinkRepository;
import io.swagger.api.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "scheduler", ignoreUnknownFields = false)
@EnableScheduling
public class LinkUpdaterScheduler {

    public enum AccessType{
        JPA, JDBC
    }

    @Value()

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
        List<LinkResponse> res;
        var res = repo1.findByTime(time);
        HashMap<Long, Collection<String>> result = new HashMap<>();
        for (Object i : res) {
            var list = (ArrayList) i;
            var current = list.get(1).toString();
            var id = Long.parseLong(list.get(0).toString());
            if (current.startsWith("https://stackoverflow.com/questions/")) {
                current = current.replace("https://stackoverflow.com/questions/", "");
                var question = Long.parseLong(current.split("/")[0]);
                var response = stack.fetchQuestion(question);
                if (Timestamp.valueOf(response.time.toLocalDateTime()).after((Timestamp) list.get(2))) {
                    if (!result.containsKey(id)) {
                        result.put(id, new ArrayList<>());
                    }
                    result.get(id).add(list.get(1).toString());
                }
            } else {
                current = current.replace("https://github.com/", "");
                var repoAuthor = current.split("/");
                var response = git.fetchRepository(repoAuthor[0], repoAuthor[1]);
                if (Timestamp.valueOf(response.time.toLocalDateTime()).after((Timestamp) list.get(2))) {
                    if (!result.containsKey(id)) {
                        result.put(id, new ArrayList<>());
                    }
                    result.get(id).add(list.get(1).toString());
                }
            }
        }
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
