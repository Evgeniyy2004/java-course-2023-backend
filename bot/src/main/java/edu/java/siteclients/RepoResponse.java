package edu.java.siteclients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class RepoResponse {
    public Owner owner;
    @JsonProperty("updated_at") public OffsetDateTime time;
    @JsonProperty("name") public String name;


    public record Owner(@JsonProperty("login") String user) {
    }


}
