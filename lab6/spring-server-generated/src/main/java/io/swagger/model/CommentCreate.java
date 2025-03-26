package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CommentCreate
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-21T18:05:28.859737022Z[GMT]")


public class CommentCreate   {
  @JsonProperty("content")

  private String content = null;

  @JsonProperty("author_id")

  private Integer authorId = null;

  @JsonProperty("post_id")

  private Integer postId = null;


  public CommentCreate content(String content) { 

    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   **/
  
  @Schema(required = true, description = "")
  
  @NotNull
  public String getContent() {  
    return content;
  }

  public CommentCreate(String content, Integer authorId, Integer postId) {
    this.content = content;
    this.authorId = authorId;
    this.postId = postId;
  }

  public void setContent(String content) {

    this.content = content;
  }

  public CommentCreate authorId(Integer authorId) { 

    this.authorId = authorId;
    return this;
  }

  /**
   * Get authorId
   * @return authorId
   **/
  
  @Schema(required = true, description = "")
  
  @NotNull
  public Integer getAuthorId() {  
    return authorId;
  }



  public void setAuthorId(Integer authorId) { 

    this.authorId = authorId;
  }

  public CommentCreate postId(Integer postId) { 

    this.postId = postId;
    return this;
  }

  /**
   * Get postId
   * @return postId
   **/
  
  @Schema(required = true, description = "")
  
  @NotNull
  public Integer getPostId() {  
    return postId;
  }



  public void setPostId(Integer postId) { 

    this.postId = postId;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentCreate commentCreate = (CommentCreate) o;
    return Objects.equals(this.content, commentCreate.content) &&
        Objects.equals(this.authorId, commentCreate.authorId) &&
        Objects.equals(this.postId, commentCreate.postId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, authorId, postId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommentCreate {\n");
    
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
    sb.append("    postId: ").append(toIndentedString(postId)).append("\n");
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
