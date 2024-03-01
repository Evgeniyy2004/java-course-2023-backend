package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LinkUpdate
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-27T13:00:16.152772335Z[GMT]")


public class LinkUpdate {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("tgChatIds")
  @Valid
  private List<Long> tgChatIds = null;

  public LinkUpdate id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LinkUpdate url(String url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   **/
  @Schema(description = "")

    public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LinkUpdate description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  @Schema(description = "")

    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LinkUpdate tgChatIds(List<Long> tgChatIds) {
    this.tgChatIds = tgChatIds;
    return this;
  }

  public LinkUpdate addTgChatIdsItem(Long tgChatIdsItem) {
    if (this.tgChatIds == null) {
      this.tgChatIds = new ArrayList<Long>();
    }
    this.tgChatIds.add(tgChatIdsItem);
    return this;
  }

  /**
   * Get tgChatIds
   * @return tgChatIds
   **/
  @Schema(description = "")

    public List<Long> getTgChatIds() {
    return tgChatIds;
  }

  public void setTgChatIds(List<Long> tgChatIds) {
    this.tgChatIds = tgChatIds;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkUpdate linkUpdate = (LinkUpdate) o;
    return Objects.equals(this.id, linkUpdate.id) &&
        Objects.equals(this.url, linkUpdate.url) &&
        Objects.equals(this.description, linkUpdate.description) &&
        Objects.equals(this.tgChatIds, linkUpdate.tgChatIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, description, tgChatIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkUpdate {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    tgChatIds: ").append(toIndentedString(tgChatIds)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
