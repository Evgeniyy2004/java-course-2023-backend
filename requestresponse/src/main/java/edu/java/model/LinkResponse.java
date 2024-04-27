package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.validation.annotation.Validated;

/**
 * LinkResponse
 */
@Validated
@Table(name="connect")
@Entity
public class LinkResponse {
    @JsonProperty("id")
    @Id
    @Column(name="id")
    private Long id = null;

    @JsonProperty("url")
    @Column(name="link")
    private String url = null;

    @Column(name="updated")
    private Timestamp time;

    public LinkResponse id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @Schema(description = "")

    public Long getId() {
        return id;
    }

    public void setTime(Timestamp time){
        this.time= time;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LinkResponse url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     **/
    @Schema(description = "")

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkResponse linkResponse = (LinkResponse) o;
        return Objects.equals(this.id, linkResponse.id)
            && Objects.equals(this.url, linkResponse.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinkResponse {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
