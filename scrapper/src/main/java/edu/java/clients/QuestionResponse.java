package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class QuestionResponse{

    @JsonProperty("items")
    private void unpackNameFromNestedObject(Object[] items) {
        isDone = (boolean)((LinkedHashMap)items[0]).get("is_answered");
        time = new Date((int)(((LinkedHashMap)items[0]).get("last_edit_date"))).toInstant()
            .atOffset(ZoneOffset.UTC);
        title = (String)((LinkedHashMap)items[0]).get("title");
        link = (String)((LinkedHashMap)items[0]).get("link");
    }
    public boolean isDone;
    public OffsetDateTime time;
    public String title;
    public String link;




}
