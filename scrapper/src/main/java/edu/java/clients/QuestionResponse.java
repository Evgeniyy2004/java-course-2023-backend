package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;

@Component
public class QuestionResponse {
    @JsonProperty("display_name") public String owner;
    @JsonProperty("last_edit_date") public OffsetDateTime time;
    @JsonProperty("is_answered") public boolean isDone;
    @JsonProperty("title") public String title;
    @JsonProperty("link") public String link;

}
