package com.moroccantube.app.service;

import com.moroccantube.app.dao.LikeDao;
import com.moroccantube.app.exception.video.VideoNotFoundException;
import com.moroccantube.app.model.Like;
import com.moroccantube.app.model.User;
import com.moroccantube.app.model.Video;
import com.moroccantube.app.repository.LikeRepository;
import com.moroccantube.app.repository.UserRepository;
import com.moroccantube.app.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;

    @Transactional
    public ResponseEntity<?> toggleLike(LikeDao likeDao) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Video video = videoRepository.findById(likeDao.getVideoId())
                .orElseThrow(() -> new VideoNotFoundException("Video not found with ID: " + likeDao.getVideoId()));

        Optional<Like> existingLike = likeRepository.findByUserAndVideo(user, video);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return ResponseEntity.ok(Map.of("message", "Disliked (unliked) successfully"));
        }

        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setVideo(video);
        newLike.setLikedAt(LocalDateTime.now());

        likeRepository.save(newLike);
        return ResponseEntity.ok(Map.of("message", "Liked successfully"));
    }
}