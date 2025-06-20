package com.moroccantube.app.controller;

import com.moroccantube.app.dao.LikeDao;
import com.moroccantube.app.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> toggleLike(@RequestBody LikeDao likeDao) {
        return likeService.toggleLike(likeDao);
    }
}
