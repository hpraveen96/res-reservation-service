package com.egov.reservationservice;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CheckAmountService checkAmountService;


    public ResponseEntity<String> createReservation(Reservation reservation,
                                                    HttpServletResponse httpServletResponse,
                                                    HttpServletRequest httpServletRequest,
                                                    String token) {

        List<Cookie> cookieList = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            cookieList = Arrays.asList(cookies);
        }else {
            cookieList = new ArrayList<>();
        }
        if(cookieList.stream().filter(cookie -> cookie.getName().equals("reservation-service-key-1")).findAny().isEmpty()) {
            Integer redisKey = new Random().nextInt();
            redisTemplate.opsForValue().set(redisKey.toString(), "Reservation in Progress");

            Cookie cookie = new Cookie("reservation-service-key-1", redisKey.toString());
            httpServletResponse.addCookie(cookie);

            HotelRoomId hotelRoomId = new HotelRoomId();
            hotelRoomId.setHotelId(reservation.getHotelId());
            hotelRoomId.setRoomId(reservation.getRoomId());

            checkAmountService.checkAmount(hotelRoomId, token, redisKey);


            return ResponseEntity.ok("Reservation in Progress");
        }else{
            String stage2responseKey = cookieList.stream().filter(cookie -> cookie.getName().equals("reservation-service-key-1")).findAny().get().getValue();
//            Based on key get value from redis
            String responseString = redisTemplate.opsForValue().get(stage2responseKey).toString();
            logger.info("Response from Redis: " + responseString);

            if(responseString.equals("Reservation in Progress"))
            {
                logger.info("Reservation in Progress. Please wait to check status");
                return ResponseEntity.ok("Reservation in Progress. Please wait to check status");
            }
            else
            {
                logger.info(responseString);
                reservationRepository.save(reservation);
                return ResponseEntity.ok(responseString);
            }
        }
    }

    public List<Reservation> getReservationsByPhone(String phone) {
        return reservationRepository.findByPhone(phone);
    }

    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }

}
