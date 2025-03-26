package io.swagger.service;

import io.swagger.api.ResourceNotFoundException;
import io.swagger.model.Comment;
import io.swagger.model.CommentCreate;
import io.swagger.model.CommentUpdate;
import io.swagger.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_WithValidData_ShouldReturnSavedComment() {
        CommentCreate request = new CommentCreate(
                "Test content",
                1,
                1
        );
        Comment expectedComment = new Comment();
        expectedComment.setContent(request.getContent());
        expectedComment.setAuthorId(request.getAuthorId());
        expectedComment.setPostId(request.getPostId());

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Comment result = commentService.createComment(request);

        assertNotNull(result.getId());
        assertEquals(request.getContent(), result.getContent());
        assertEquals(request.getAuthorId(), result.getAuthorId());
        assertEquals(request.getPostId(), result.getPostId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void getCommentById_WhenExists_ShouldReturnComment() {
        Long commentId = 1L;
        Comment expected = new Comment();
        expected.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expected));

        Comment result = commentService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
    }

    @Test
    void updateComment_WithValidData_ShouldUpdateComment() {
        Long commentId = 1L;
        CommentUpdate updateRequest = new CommentUpdate();
        updateRequest.setContent("Updated content");
        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setContent("Old content");
        existingComment.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        Comment result = commentService.updateComment(commentId, updateRequest);

        assertEquals(updateRequest.getContent(), result.getContent());
        assertTrue(result.getUpdatedAt().isAfter(result.getCreatedAt()));
    }

    @Test
    void deleteComment_WhenExists_ShouldDeleteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(commentId);
    }

}
