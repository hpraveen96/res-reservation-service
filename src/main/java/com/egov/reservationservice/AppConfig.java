package com.egov.reservationservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class AppConfig {

    @Autowired
    EurekaDiscoveryClient discoveryClient;

    @Bean
    public WebClient authValidateWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("auth-service");
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());

        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/api/v1/validate", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean
    public WebClient getAmountWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("res-hotelmanagement-service");
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());

        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/api/v1/get/amount", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }
}
