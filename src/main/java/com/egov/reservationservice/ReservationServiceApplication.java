package com.egov.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ReservationServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

}
