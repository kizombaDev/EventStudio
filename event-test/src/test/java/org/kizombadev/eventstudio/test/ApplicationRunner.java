package org.kizombadev.eventstudio.test;

import org.kizombadev.eventstudio.apps.restapiapp.RestApiApp;
import org.kizombadev.eventstudio.eventpipeline.EventPipeline;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationRunner implements Cloneable{

    private ConfigurableApplicationContext eventPipeline = null;
    private ConfigurableApplicationContext restApiApp = null;


    public void initEventPipelineApplication() {
        eventPipeline = new SpringApplicationBuilder(EventPipeline.class).run(new String[]{"--server.port=8081"});
    }

    public void initRestApiApplication() {
        restApiApp = new SpringApplicationBuilder(RestApiApp.class).run("--server.port=8082",
                "--backend.indexname=ping",
                "--backend.clustername=elasticsearch",
                "--backend.nodes[0].ip=localhost",
                "--backend.nodes[0].port=9300");
    }

    public void close() {
        if(eventPipeline != null) {
            eventPipeline.close();
            eventPipeline = null;
        }

        if(restApiApp != null) {
            restApiApp.close();
            restApiApp = null;
        }
    }

    public void waitForResult() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}