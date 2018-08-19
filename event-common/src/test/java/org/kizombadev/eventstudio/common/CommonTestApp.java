package org.kizombadev.eventstudio.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.kizombadev.eventstudio"})
public class CommonTestApp {

    public static void main(String[] args) {
        SpringApplication.run(CommonTestApp.class, args);
    }
}