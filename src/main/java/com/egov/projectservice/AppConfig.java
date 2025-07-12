package com.egov.projectservice;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient authValidateWebClient(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/api/v1/validate", "localhost", "8091"))
                .filter(new LoggingWebClientFilter()) // FOR OBSERVABILITY RELATED FEATURES
                .build();
    }
}
