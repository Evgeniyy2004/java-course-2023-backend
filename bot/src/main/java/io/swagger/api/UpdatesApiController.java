package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.configuration.Bot;
import edu.java.model.LinkUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
                            date = "2024-02-27T13:00:16.152772335Z[GMT]")
@RestController
public class UpdatesApiController implements UpdatesApi {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatesApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    Bot bot;

    @org.springframework.beans.factory.annotation.Autowired
    public UpdatesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;

    }

    public ResponseEntity updatesPost(
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        LinkUpdate body
    ) {
        bot.execute(new SendMessage(body.getId(), "По ссылке " + body.getUrl() + "появилось обновление"));
        return new ResponseEntity(HttpStatus.OK);
    }

}
