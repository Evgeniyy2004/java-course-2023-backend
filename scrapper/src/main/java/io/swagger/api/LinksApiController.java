package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.AddLinkRequest;
import io.swagger.model.LinkResponse;
import io.swagger.model.ListLinksResponse;
import io.swagger.model.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.net.URISyntaxException;
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

    private final ObjectMapper objectMapper;

    @Autowired
    private JdbcLinkService linkService;
    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LinksApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @SuppressWarnings("MultipleStringLiterals")
    public ResponseEntity linksDelete(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        RemoveLinkRequest body
    ) {
        try {
            var uri = new URI(body.getLink());
            linkService.remove(tgChatId, uri);
        } catch (ApiException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity linksGet(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        Object[] res1;
        try {
            var res = linkService.listAll(tgChatId).toArray();
            res1 = res;
        } catch (ApiException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var result = new ListLinksResponse();
        for (int u = 0; u < res1.length; u++) {
            var curr = new LinkResponse();
            curr.setUrl(res1[u].toString());
            result.addLinksItem(curr);
        }
        return new ResponseEntity<ListLinksResponse>(result, HttpStatus.OK);

    }

    public ResponseEntity linksPost(
        @Parameter(in = ParameterIn.HEADER, description = "", required = true, schema = @Schema())
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        AddLinkRequest body
    ) {
        try {
            var uri = new URI(body.getLink());
            linkService.add(tgChatId, uri);
        } catch (ApiException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
