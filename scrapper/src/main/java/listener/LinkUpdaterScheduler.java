package listener;

import edu.java.botclient.UpdatesClient;
import edu.java.configuration.Configuration;
import io.swagger.api.LinkRepository;
import io.swagger.api.LinksApi;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
@Log
public class LinkUpdaterScheduler {
    @Autowired
    private UpdatesClient client;

    @Autowired
    private LinkRepository links;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        var toDo = links.u
    }
}
