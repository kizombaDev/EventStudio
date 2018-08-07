package org.kizombadev.eventstudio.eventpipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
public class EventPipeline {

    public static void main(String[] args) {
        SpringApplication.run(EventPipeline.class, args);
    }
}