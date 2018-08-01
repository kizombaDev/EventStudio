package org.kizombadev.eventstudio.clients.pingclient.operation;

import org.kizombadev.eventstudio.clients.pingclient.output.OutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component("WindowsPingOperation")
public class WindowsPingOperation implements ClientOperation {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String id;
    private String host;
    private OutputService outputService;

    @Override
    public void run() {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commandAndArguments = {"ping", host, "-n", "1"};

            Process p = rt.exec(commandAndArguments);

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            int lineIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (lineIndex != 2) {
                    lineIndex++;
                    continue;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("origin", line);
                data.put("type", "ping");
                data.put("source_id", "ping_" + id);
                data.put("timestamp", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
                outputService.handleSend(data);
                break;
            }
            reader.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    @Override
    public void init(String id, OutputService outputService, Map<String, String> configurationAsMap) {
        this.outputService = outputService;
        this.host = configurationAsMap.get("host");
        this.id = id;
    }

    @Override
    public ClientOperation instanceCopy() {
        return new WindowsPingOperation();
    }
}
