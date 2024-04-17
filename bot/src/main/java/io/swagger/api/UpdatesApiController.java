package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.LinkUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatesApiController implements UpdatesApi {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatesApiController.class);

    private final ObjectMapper objectMapper;

    private Listener listener;

    @org.springframework.beans.factory.annotation.Autowired
    public UpdatesApiController(ObjectMapper objectMapper, Listener listener) {

        this.objectMapper = objectMapper;
        this.listener = listener;
    }

    public ResponseEntity updatesPost(
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody
        LinkUpdate body
    ) {
        listener.send(body);
        return new ResponseEntity(HttpStatus.OK);
    }

}
