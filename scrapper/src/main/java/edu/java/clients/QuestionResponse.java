package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class QuestionResponse {
    @JsonProperty("display_name") String owner;
    @JsonProperty("last_edit_date") OffsetDateTime time;
    @JsonProperty("is_answered") boolean isDone;
    @JsonProperty("title") String title;
    @JsonProperty("link") String link;

}
