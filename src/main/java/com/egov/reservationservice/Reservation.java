package com.egov.reservationservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;
    private String phone;
    private String roomId;
    private String hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;
    private LocalDateTime createdAt;

}
