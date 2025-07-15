package com.egov.reservationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class CheckAmountService {

    private static final Logger logger = LoggerFactory.getLogger(CheckAmountService.class);

    @Autowired
    @Qualifier("getAmountWebClient")
    WebClient getAmountWebClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void checkAmount(HotelRoomId hotelRoomId,String token, Integer redisKey) throws WebClientResponseException
    {
        logger.info("Record message called with token: " + token);
        Mono response =  getAmountWebClient.post()
                .header("Authorization", token)
                .bodyValue(hotelRoomId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    // Handle or log the error here
                    logger.error("Error occurred while sending request to get amount", error);
                });

        response.subscribe(
                responseVal -> {
                    // Handle the successful response here
                    logger.info("Successfully received response: {}", responseVal.toString());

                    redisTemplate.opsForValue().set(redisKey.toString(), responseVal);
                },
                error -> {
                    // Handle or log the error here
                    logger.error("Error occurred while sending request to quote service {}", error);
                });

    }
}
