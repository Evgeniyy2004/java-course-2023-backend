package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class RepoResponse {
    @JsonProperty("login") public String owner;
    @JsonProperty("updated_at") public OffsetDateTime time;
    @JsonProperty("name") public String name;
    //@JsonProperty("html_url") public String link;
}
