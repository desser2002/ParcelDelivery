package org.dzianisbova.parceldelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParcelDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParcelDeliveryApplication.class, args);
    }
}
