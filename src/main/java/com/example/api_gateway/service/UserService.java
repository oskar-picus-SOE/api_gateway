package com.example.api_gateway.service;

import com.example.api_gateway.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class UserService {
    private final WebClient.Builder loadBalancedWebClientBuilder;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserService(WebClient.Builder loadBalancedWebClientBuilder) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
    }

    public Optional<User> findUser(String username) {
        ResponseEntity<User> response = loadBalancedWebClientBuilder.build()
                .get()
                .uri(userServiceUrl, uri -> uri.queryParam("username", username).build())
                .retrieve()
                .toEntity(User.class)
                .block();

        if (response == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(response.getBody());
    }
}
