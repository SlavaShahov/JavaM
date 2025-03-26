package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Comment;
import io.swagger.model.CommentCreate;
import io.swagger.model.CommentUpdate;
import io.swagger.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsApiController.class)
class CommentsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private final Long testCommentId = 1L;
    private final Comment testComment = Comment.builder()
            .id(testCommentId)
            .content("Test content")
            .authorId(1)
            .postId(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    @Test
    void createComment_ShouldReturnCreated() throws Exception {
        CommentCreate createRequest = new CommentCreate("New comment", 1, 1);

        Mockito.when(commentService.createComment(any(CommentCreate.class)))
                .thenReturn(testComment);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void getCommentById_ShouldReturnComment() throws Exception {
        Mockito.when(commentService.getCommentById(testCommentId)).thenReturn(testComment);

        mockMvc.perform(get("/comments/{commentId}", testCommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void updateComment_ShouldReturnUpdatedComment() throws Exception {
        CommentUpdate updateRequest = new CommentUpdate();
        updateRequest.setContent("Updated content");

        Comment updatedComment = Comment.builder()
                .id(testCommentId)
                .content("Updated content")
                .authorId(1)
                .postId(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(commentService.updateComment(anyLong(), any(CommentUpdate.class)))
                .thenReturn(updatedComment);

        mockMvc.perform(put("/comments/{commentId}", testCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void deleteComment_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/comments/{commentId}", testCommentId))
                .andExpect(status().isNoContent());

        Mockito.verify(commentService, Mockito.times(1))
                .deleteComment(testCommentId);
    }
}
