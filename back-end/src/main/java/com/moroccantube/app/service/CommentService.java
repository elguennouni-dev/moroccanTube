package com.moroccantube.app.service;

import com.moroccantube.app.dao.CommentDao;
import com.moroccantube.app.dto.CommentDto;
import com.moroccantube.app.exception.comment.CommentNotFoundException;
import com.moroccantube.app.exception.video.VideoNotFoundException;
import com.moroccantube.app.mapper.CommentMapper;
import com.moroccantube.app.model.Comment;
import com.moroccantube.app.model.User;
import com.moroccantube.app.model.Video;
import com.moroccantube.app.repository.CommentRepository;
import com.moroccantube.app.repository.UserRepository;
import com.moroccantube.app.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CommentMapper commentMapper;


    public ResponseEntity<?> handleComment(CommentDao commentDao) {
        Optional<User> userOptional = userRepository.findByUsername(commentDao.getUsername());
        Optional<Video> videoOptional = videoRepository.findById(commentDao.getVideoId());

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username : " + commentDao.getUsername());
        }

        if (videoOptional.isEmpty()) {
            throw new VideoNotFoundException("No video found with ID: " + commentDao.getVideoId());
        }

        Comment comment = new Comment();
        comment.setContent(commentDao.getComment());
        comment.setUser(userOptional.get());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setVideo(videoOptional.get());

        Comment savedComment = commentRepository.save(comment);

        CommentDto commentDto = commentMapper.toDto(savedComment);
        commentDto.setUsername(userOptional.get().getUsername());

        return ResponseEntity.ok(commentDto);
    }


    public ResponseEntity<?> handleDelete(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CommentNotFoundException("No comment found with this ID: " + commentId);
        }

        commentRepository.delete(commentOptional.get());

        return ResponseEntity.ok(Map.of("message","Comment deleted successfully!"));
    }

}
