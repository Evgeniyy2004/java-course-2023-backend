package listener;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "${app.scheduler.delay}")
    public void update (){
    }
}
