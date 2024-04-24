package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.ApiErrorResponse;
import edu.java.model.ApiException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
@ConfigurationProperties(prefix="chat")
public class TgChatApiController implements TgChatApi {

    private static final Logger LOG = LoggerFactory.getLogger(TgChatApiController.class);

    private static final String HEADER = "Accept";


    private ObjectMapper objectMapper;

    private HttpServletRequest request;


    private JdbcTgChatService chatService;

    public enum AccessType {
        JDBC, JPA,
    }

    @Value("${chat.use}")
    private AccessType type;

    @org.springframework.beans.factory.annotation.Autowired
    public TgChatApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Valid
    public ResponseEntity tgChatIdDelete(
        @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("id")
        Long id
    ) {
        try {
            chatService.unregister(id);
        } catch (ApiException e) {
            var res = new ResponseEntity<ApiErrorResponse>(HttpStatus.NOT_FOUND);
            return res;
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Valid
    public ResponseEntity tgChatIdPost(
        @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("id")
        Long id
    ) {
        try {
            chatService.register(id);
        } catch (ApiException e) {
            var res = new ResponseEntity<ApiErrorResponse>(HttpStatus.CONFLICT);
            return res;
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
