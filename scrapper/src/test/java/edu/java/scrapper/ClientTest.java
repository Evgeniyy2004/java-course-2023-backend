package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.configuration.ClientConfiguration;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@WireMockTest
@SpringBootTest
@Log
public class ClientTest {
    //WireMockServer wireMockServer = new WireMockServer();

    @Test
    public  void gitTest() {
        var response = new ClientConfiguration().beanGit().fetchRepository("jojozhuang","algorithm-problems-java");
        log.info(String.valueOf(response.time));

    }

    @Test
    public  void stackTest() {
        var response = new ClientConfiguration().beanStack().fetchQuestion(6827752);
        log.info(String.valueOf(response.isDone));

    }
}

