package org.kizombadev.eventstudio.apps.extendedanalyseapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExtendedAnalyseApp {

    public static void main(String[] args) {
        SpringApplication.run(ExtendedAnalyseApp.class, args);
    }
}
