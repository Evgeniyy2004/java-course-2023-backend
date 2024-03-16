package listener;

import edu.java.botclient.UpdatesClient;
import io.swagger.api.LinkRepository;
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

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        var allchanges = repo.update();
        for (Long id : allchanges.keySet()) {

        }
    }
}
