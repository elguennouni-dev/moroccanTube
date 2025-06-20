package com.moroccantube.app.controller;

import com.moroccantube.app.dao.CommentDao;
import com.moroccantube.app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping()
    public ResponseEntity<?> createComment(@RequestBody CommentDao commentDao) {
        return commentService.handleComment(commentDao);
    }


    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        return commentService.handleDelete(commentId);
    }



}
