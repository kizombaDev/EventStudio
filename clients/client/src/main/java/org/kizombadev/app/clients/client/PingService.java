package org.kizombadev.app.clients.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PingService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private PipelineClient pipelineClient;

    @Autowired
    public PingService(PipelineClient pipelineClient) {
        this.pipelineClient = pipelineClient;
    }

    public void execute(String url, String idPostFix) {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commandAndArguments = {"ping", url, "-n", "1"};

            Process p = rt.exec(commandAndArguments);

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {

                if(!line.startsWith("Antwort"))
                {
                    continue;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("origin", line);
                data.put("type", "ping");
                data.put("id", "ping_" + idPostFix);
                data.put("timestamp", formatter.format(LocalDateTime.now()));
                pipelineClient.send(data);
            }
            reader.close();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

}
