package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@WireMockTest
@SpringBootTest
public class ClientTest {
    WireMockServer wireMockServer = new WireMockServer();

    @Test
    public  void gitTest() {
        wireMockServer.stubFor()
    }
}

