package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.AddLinkRequest;
import edu.java.model.ApiException;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
                            date = "2024-02-27T16:17:37.541889551Z[GMT]")
@RestController
public class LinksApiController implements LinksApi {

    private static final Logger LOG = LoggerFactory.getLogger(LinksApiController.class);

    private static final int NOT_FOUND = 404;
    @Autowired
    private JdbcLinkService linkService;
    private final ObjectMapper objectMapper;
    private final Bucket bucket;
    private final HttpServletRequest request;

    private static final int ERROR = 404;

    @org.springframework.beans.factory.annotation.Autowired
    public LinksApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        Bandwidth limit =
            Bandwidth.classic(2 * 2 * 2 * 2 + 2 * 2, Refill.greedy(2 * 2 * 2 * 2 + 2 * 2, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @SuppressWarnings("MultipleStringLiterals")
    public ResponseEntity<LinkResponse> linksDelete(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        RemoveLinkRequest body
    ) {
        if (bucket.tryConsume(1)) {
            try {
                linkService.remove(tgChatId, body.getLink());
            } catch (ApiException e) {
                if (e.getCode() == ERROR) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity(HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    public ResponseEntity<ListLinksResponse> linksGet(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        if (bucket.tryConsume(1)) {
            try {
                var res = linkService.listAll(tgChatId);
                ListLinksResponse response = new ListLinksResponse();
                for (URI uri : res) {
                    var curr = new LinkResponse();
                    curr.setUrl(uri.toString());
                    response.addLinksItem(curr);
                }
                return new ResponseEntity<ListLinksResponse>(response, HttpStatus.OK);
            } catch (ApiException e) {
                if (e.getCode() == ERROR) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity(HttpStatus.CONFLICT);
                }
            }
        } else {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    public ResponseEntity<LinkResponse> linksPost(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        AddLinkRequest body
    ) {
        if (bucket.tryConsume(1)) {
            try {
                linkService.add(tgChatId, body.getLink());
            } catch (ApiException e) {
                if (e.getCode() == ERROR) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity(HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

}
