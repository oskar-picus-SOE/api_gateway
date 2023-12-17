package com.example.api_gateway.service;

import com.example.api_gateway.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final RestTemplate restTemplate;
    private final Environment environment;

    @Value("${user.service.url}")
    private String userServiceUrl;
    @Value("${user.service.addresses}")
    private String[] userServiceAddresses;

    public UserService(WebClient.Builder loadBalancedWebClientBuilder, RestTemplate restTemplate, Environment environment) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public Optional<User> findUser(String username) {
        boolean isProd = Arrays.asList(environment.getActiveProfiles()).contains("prod");

        if (isProd) {
            String host = Math.random() < 0.5 ? userServiceAddresses[0] : userServiceAddresses[1];
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host(host)
                    .path("/api/users")
                    .port(8081)
                    .queryParam("username", username)
                    .build()
                    .toUri();
            LOGGER.info("Finding user at URL {}", uri);
            return Optional.ofNullable(restTemplate.getForObject(uri, User.class));
        }

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
