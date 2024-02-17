package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class RepoResponse {
    @JsonProperty("owner") String owner;
    @JsonProperty("updated_at") OffsetDateTime time;
    @JsonProperty("name") String name;

    @JsonProperty("html_url") String link;
}
