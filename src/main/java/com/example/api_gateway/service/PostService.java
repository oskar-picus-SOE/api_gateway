package com.example.api_gateway.service;

import com.example.api_gateway.entity.Post;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Value("${post.service.exchange.name}")
    private String exchangeName;
    @Value("${post.service.routing.key.name}")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PostService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addPost(Post post) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, post);
    }
}
