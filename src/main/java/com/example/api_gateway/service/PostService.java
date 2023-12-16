package com.example.api_gateway.service;

import com.example.api_gateway.entity.Post;
import com.example.api_gateway.entity.PostsList;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostService {
    @Value("${post.service.exchange.name}")
    private String exchangeName;
    @Value("${post.service.routing.key.name}")
    private String routingKey;
    @Value("${post.service.address}")
    private String postServiceAddress;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PostService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addPost(Post post) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, post);
    }

    public PostsList getAll() {
        return restTemplate.getForObject(String.format("%s/api/posts", postServiceAddress), PostsList.class);
    }
}
