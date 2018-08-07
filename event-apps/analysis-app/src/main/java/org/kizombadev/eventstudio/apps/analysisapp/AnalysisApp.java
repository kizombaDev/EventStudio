package org.kizombadev.eventstudio.apps.analysisapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
public class AnalysisApp {

    public static void main(String[] args) {
        SpringApplication.run(AnalysisApp.class, args);
    }

}
