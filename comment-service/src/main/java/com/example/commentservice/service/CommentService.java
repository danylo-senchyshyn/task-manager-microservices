package com.example.commentservice.service;

import com.example.commentservice.dto.CommentDTO;
import com.example.commentservice.entity.Comment;
import com.example.commentservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setTaskId(comment.getTaskId());
        dto.setUserId(comment.getUserId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    private Comment mapToEntity(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setTaskId(dto.getTaskId());
        comment.setUserId(dto.getUserId());
        comment.setText(dto.getText());
        comment.setCreatedAt(dto.getCreatedAt());
        comment.setUpdatedAt(dto.getUpdatedAt());
        return comment;
    }

    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
        return mapToDTO(comment);
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);
        Comment saved = commentRepository.save(comment);
        return mapToDTO(saved);
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
        existing.setText(commentDTO.getText());
        existing.setTaskId(commentDTO.getTaskId());
        existing.setUserId(commentDTO.getUserId());
        Comment updated = commentRepository.save(existing);
        return mapToDTO(updated);
    }

    public void deleteComment(Long id) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
        commentRepository.delete(existing);
    }

    public void reset() {
        commentRepository.deleteAll();
    }
}