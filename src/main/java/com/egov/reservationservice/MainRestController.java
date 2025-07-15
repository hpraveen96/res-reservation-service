package com.egov.reservationservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class MainRestController {
    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    ReservationService reservationService;


    @GetMapping("bookRoom/{userId}")
    public ResponseEntity<String> addProfile(@RequestHeader("Authorization") String token,
                                             @RequestBody Reservation reservation,
                                             HttpServletResponse httpServletResponse,
                                             HttpServletRequest httpServletRequest,
                                             @PathVariable String userId) {
        logger.info("Received parameter for profile"+ reservation.toString());
        String phone="";
        try {
            phone = tokenService.validateToken(token);
        }catch(WebClientResponseException e){
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }
        if(phone.equals(userId)) {
            logger.info("Phone Match, Saving Reservation details");

            return reservationService.createReservation(reservation,httpServletResponse,httpServletRequest,token);
        }

        return ResponseEntity.status(401).body("Invalid Phone Number");

    }

    @GetMapping("bookRoom/details")
    public ResponseEntity<?> getByCustomer(@RequestHeader("Authorization") String token,
                                                           @RequestParam String CustomerPhone) {

        logger.info("Received parameter for phone"+ CustomerPhone);
        String phone="";
        try {
            phone = tokenService.validateToken(token);
        }catch(WebClientResponseException e){
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }
        if(phone.equals(CustomerPhone)) {
            List<Reservation> reservation = reservationRepository.findByPhone(phone);
            return ResponseEntity.ok(reservation);
        }

        return ResponseEntity.status(401).body("Invalid Phone Number");
    }

}
