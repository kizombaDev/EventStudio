package org.kizombadev.app.clients.client.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutputServiceImpl implements OutputService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private List<ClientOutput> clientOutputList;

    @Autowired
    public OutputServiceImpl(List<ClientOutput> clientOutputList) {
        this.clientOutputList = clientOutputList;
    }

    @Override
    public void handleSend(Map<String, Object> data) {
        for (ClientOutput output : clientOutputList) {
            output.send(data);
        }
    }
}
