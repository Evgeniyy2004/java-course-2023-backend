package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

@Component
public class QuestionResponse{

    public String title;
    public  boolean isDone;
    public String link;
    public String time;
    @JsonProperty("items")
    public void All( Object[] all) {
        this.title = (String)((Map<String,Object>) all[0]).get("title");
        this.isDone =(boolean)((Map<String,Object>) all[0]).get("is_answered");
        this.link = (String)((Map<String,Object>) all[0]).get("link");
        this.time = (String)((Map<String,Object>) all[0]).get("last_edit_date");
    }


}
