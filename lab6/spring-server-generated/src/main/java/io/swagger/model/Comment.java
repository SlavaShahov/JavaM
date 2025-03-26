package io.swagger.model;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Comment
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-21T18:05:28.859737022Z[GMT]")

@Setter
@Getter
@Entity
@Builder
@Table(name = "comments")
@AllArgsConstructor
public class Comment   {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("content")

  private String content = null;

  @JsonProperty("author_id")

  private Integer authorId = null;

  @JsonProperty("post_id")

  private Integer postId = null;

  @JsonProperty("created_at")

  private LocalDateTime createdAt = null;

  @JsonProperty("updated_at")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private LocalDateTime updatedAt = null;

  public Comment() {}

//  public Comment(String content, Integer authorId, Integer postId) {
//    this.content = content;
//    this.authorId = authorId;
//    this.postId = postId;
//  }


  public Comment id(Long id) {

    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  
  @Schema(example = "1", required = true, description = "")
  
 // @NotNull
  public Long getId() {  
    return id;
  }

  public void setId(Long id) { 

    this.id = id;
  }

  public Comment content(String content) { 

    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   **/
  
  @Schema(example = "Это пример комментария", required = true, description = "")
  
  @NotNull
  public String getContent() {  
    return content;
  }



  public void setContent(String content) { 

    this.content = content;
  }

  public Comment authorId(Integer authorId) { 

    this.authorId = authorId;
    return this;
  }

  /**
   * Get authorId
   * @return authorId
   **/
  
  @Schema(example = "123", required = true, description = "")
  
  @NotNull
  public Integer getAuthorId() {  
    return authorId;
  }



  public void setAuthorId(Integer authorId) { 

    this.authorId = authorId;
  }

  public Comment postId(Integer postId) { 

    this.postId = postId;
    return this;
  }

  /**
   * Get postId
   * @return postId
   **/
  
  @Schema(example = "456", required = true, description = "")
  
  @NotNull
  public Integer getPostId() {  
    return postId;
  }



  public void setPostId(Integer postId) { 

    this.postId = postId;
  }

  public Comment createdAt(LocalDateTime createdAt) {

    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   **/
  
  @Schema(example = "2023-10-01T12:00Z", required = true, description = "")
  
@Valid
  @NotNull
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }



  public void setCreatedAt(LocalDateTime createdAt) {

    this.createdAt = createdAt;
  }

  public Comment updatedAt(LocalDateTime updatedAt) {

    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   **/
  
  @Schema(example = "2023-10-01T12:30Z", description = "")
  
@Valid
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }



  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.id, comment.id) &&
        Objects.equals(this.content, comment.content) &&
        Objects.equals(this.authorId, comment.authorId) &&
        Objects.equals(this.postId, comment.postId) &&
        Objects.equals(this.createdAt, comment.createdAt) &&
        Objects.equals(this.updatedAt, comment.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, authorId, postId, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
    sb.append("    postId: ").append(toIndentedString(postId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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
