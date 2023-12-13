package com.example.api_gateway.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerInstanceConfiguration {
    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier() {
        return new InstanceSupplier("user-service");
    }
}
