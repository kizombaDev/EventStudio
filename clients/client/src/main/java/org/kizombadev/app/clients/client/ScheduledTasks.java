package org.kizombadev.app.clients.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private PingService pingService;

    @Autowired
    public ScheduledTasks(PingService pingService){
        this.pingService = pingService;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        pingService.execute("www.google.de", "google");
        pingService.execute("www.facebook.de", "facebook");
        pingService.execute("www.fau.de", "fau");
        pingService.execute("127.0.0.1", "localhost");
    }
}
