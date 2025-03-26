package io.swagger.service;

import io.swagger.api.ResourceNotFoundException;
import io.swagger.model.Comment;
import io.swagger.model.CommentCreate;
import io.swagger.model.CommentUpdate;
import io.swagger.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment updateComment(Long commentId, CommentUpdate commentUpdate) {
        log.info("Updating comment with ID: {}", commentId);
        Comment existingComment = getCommentById(commentId);
        existingComment.setContent(commentUpdate.getContent());
        existingComment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(existingComment);
        log.info("Comment with ID {} updated successfully", commentId);
        return updatedComment;
    }

    public Comment createComment(CommentCreate commentCreate) {
        log.info("Attempting to create comment: {}", commentCreate);
        Comment comment = new Comment();
        comment.setContent(commentCreate.getContent());
        comment.setAuthorId(commentCreate.getAuthorId());
        comment.setPostId(commentCreate.getPostId());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created successfully with ID: {}", savedComment.getId());
        return savedComment;
    }

    public Comment getCommentById(Long commentId) {
        log.info("Requesting comment with ID: {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.error("Comment with ID {} not found", commentId);
                    return new ResourceNotFoundException("Comment not found");
                });
    }

    public void deleteComment(Long commentId) {
        log.info("Deleting comment with ID: {}", commentId);
        Comment comment = getCommentById(commentId);
        commentRepository.delete(comment);
        log.info("Comment with ID {} deleted successfully", commentId);
    }
}