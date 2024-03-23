package listener;

import edu.java.botclient.UpdatesClient;
import io.swagger.api.LinkRepository;
import model.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LinkUpdaterScheduler {

    @Autowired
    UpdatesClient client;

    @Autowired
    LinkRepository repo;

    @Scheduled(fixedDelayString = "86400s")
    public void update() {
        var allChanges = repo.update();
        for (Long id : allChanges.keySet()) {
            var request = new LinkUpdate();
            request.setId(id);
            for (String url : allChanges.get(id)) {
                request.setUrl(url);
                client.post(request);
            }
        }
    }
}
