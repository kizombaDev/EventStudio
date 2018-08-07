package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
@EnableScheduling
public class ExtendedAnalysisApp {

    public static void main(String[] args) {
        SpringApplication.run(ExtendedAnalysisApp.class, args);
    }
}
