package org.kizombadev.eventstudio.clients.pingclient.output;

import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutputServiceImpl implements OutputService {
    private List<ClientOutput> clientOutputList;

    @Autowired
    public OutputServiceImpl(List<ClientOutput> clientOutputList) {
        this.clientOutputList = clientOutputList;
    }

    @Override
    public void handleSend(Map<EventKeys, Object> data) {
        for (ClientOutput output : clientOutputList) {
            output.send(data);
        }
    }
}
