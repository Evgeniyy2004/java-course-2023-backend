package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.ApiErrorResponse;
import edu.java.model.ApiException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "chat")
public class TgChatApiController implements TgChatApi {

    private static final Logger LOG = LoggerFactory.getLogger(TgChatApiController.class);

    private static final String HEADER = "Accept";

    private ObjectMapper objectMapper;

    private HttpServletRequest request;


    @Autowired
    private JdbcTgChatService jdbcService;

    @Autowired
    private JpaChatService jpaService;

    public enum AccessType {
        JDBC, JPA,
    }

    @Value("${chat.use}")
    private AccessType type;
    private final Bucket bucket;



    @org.springframework.beans.factory.annotation.Autowired
    public TgChatApiController(
        ObjectMapper objectMapper,
        HttpServletRequest request,
        JpaChatService jpa,
        JdbcTgChatService jdbc
    ) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.jpaService = jpa;
        this.jdbcService = jdbc;
        Bandwidth limit =
            Bandwidth.classic(2 * 2 * 2 * 2 + 2 * 2, Refill.greedy(2 * 2 * 2 * 2 + 2 * 2, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @Valid
    public ResponseEntity tgChatIdDelete(
        @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("id")
        Long id
    ) {

        if (bucket.tryConsume(1)) {
            try {
                if (type == AccessType.JDBC) {
                jdbcService.unregister(id);
            } else {
                jpaService.unregister(id);
            }
            } catch (ApiException e) {
                var res = new ResponseEntity<ApiErrorResponse>(HttpStatus.NOT_FOUND);
                return res;
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    @Valid
    public ResponseEntity tgChatIdPost(
        @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("id")
        Long id
    ) {

        if (bucket.tryConsume(1)) {
            try {
                if (type == AccessType.JDBC) {
                jdbcService.register(id);
            } else {
                jpaService.register(id);
            }
            } catch (ApiException e) {
                var res = new ResponseEntity<ApiErrorResponse>(HttpStatus.CONFLICT);
                return res;
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

}
