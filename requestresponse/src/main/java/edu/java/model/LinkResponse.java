package edu.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import org.springframework.validation.annotation.Validated;

/**
 * LinkResponse
 */
@Validated
@Table(name = "connect")
@Entity
public class LinkResponse {

    @EmbeddedId
    private IdLink key;

    @Column(name = "updated")
    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public IdLink getKey() {
        return key;
    }

    public void setUrl(String link) {
        key.setUrl(link);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinkResponse {\n");

        sb.append("    id: ").append(toIndentedString(key.getId())).append("\n");
        sb.append("    url: ").append(toIndentedString(key.getUrl())).append("\n");
        sb.append("    time: ").append(toIndentedString(time)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
