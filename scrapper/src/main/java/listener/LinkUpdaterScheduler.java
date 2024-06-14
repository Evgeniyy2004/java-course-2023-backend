package listener;

import edu.java.botclient.UpdatesClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.model.LinkUpdate;
import io.swagger.api.JdbcLinkRepository;
import io.swagger.api.ScrapperQueueProducer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LinkUpdaterScheduler {

    private final UpdatesClient client;

    private final ScrapperQueueProducer queue;

    private final JdbcLinkRepository repo;

    public LinkUpdaterScheduler(UpdatesClient client, ScrapperQueueProducer queue, JdbcLinkRepository repo) {
        this.repo = repo;
        this.queue = queue;
        this.client = client;
    }

    @Scheduled(fixedDelay = 3600000)
    public void update() {
        var allChanges = repo.update();
        for (Long id : allChanges.keySet()) {
            var request = new LinkUpdate();
            request.setId(id);
            for (String url : allChanges.get(id)) {
                request.setUrl(url);
                if (ApplicationConfig.isUseQueue()) {
                    queue.send(request);
                } else {
                    client.post(request);
                }
            }
        }
    }
}
