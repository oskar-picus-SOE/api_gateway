package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

public class InstanceSupplier implements ServiceInstanceListSupplier {
    private final String serviceId;

    @Value("${user.service.ports}")
    private int[] userServicePorts;
    @Value("${user.service.addresses}")
    private String[] userServiceAddresses;

    public InstanceSupplier(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> defaultServiceInstances = IntStream.range(0, Math.min(userServicePorts.length, userServiceAddresses.length))
                .mapToObj(i -> (ServiceInstance) new DefaultServiceInstance(
                        String.format("%s%s", serviceId, i),
                        serviceId,
                        userServiceAddresses[i],
                        userServicePorts[i],
                        false
                ))
                .toList();
        return Flux.just(defaultServiceInstances);
    }
}
