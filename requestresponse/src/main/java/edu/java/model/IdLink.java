package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class IdLink implements Serializable {

    @JsonProperty("url")
    @Column(name = "link")
    private String url;

    @JsonProperty("id")
    @Column(name = "id")
    private Long id;

    public IdLink(String url, Long id) {
        this.id = id;
        this.url = url;
    }

    public IdLink() {
    }

    public void setUrl(String link) {
        this.url = link;
    }

    public long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }
}
