package com.example.api_gateway.controller;

import com.example.api_gateway.entity.Post;
import com.example.api_gateway.entity.PostsList;
import com.example.api_gateway.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class PostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> addPost(@RequestBody Post post) {
        LOGGER.info("Adding post {}", post);
        postService.addPost(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsList> getAllPosts() {
        return ResponseEntity.ok(postService.getAll());
    }
}
