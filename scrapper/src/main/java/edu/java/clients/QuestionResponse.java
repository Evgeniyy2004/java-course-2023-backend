package edu.java.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashMap;

public class QuestionResponse {

    @JsonProperty("items")
    private void unpackNameFromNestedObject(Object[] items) {
        if (items.length != 0) {
            var map = (LinkedHashMap) items[0];
            isDone = (boolean) (map).get("is_answered");
            time = new Date((int) ((map).get("last_edit_date"))).toInstant()
                .atOffset(ZoneOffset.UTC);
            title = (String) (map).get("title");
            link = (String) (map).get("link");
        }
    }

    public boolean isDone;
    public OffsetDateTime time;
    public String title;
    public String link;

}
