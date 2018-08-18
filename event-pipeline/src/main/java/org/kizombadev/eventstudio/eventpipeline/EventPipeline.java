package org.kizombadev.eventstudio.eventpipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
public class EventPipeline {

    public static void main(String[] args) {
        SpringApplication.run(EventPipeline.class, args);
    }
}