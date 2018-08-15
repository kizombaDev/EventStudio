package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
@EnableScheduling
public class HouseKeepingApp {

    public static void main(String[] args) {
        SpringApplication.run(HouseKeepingApp.class, args);
    }
}
