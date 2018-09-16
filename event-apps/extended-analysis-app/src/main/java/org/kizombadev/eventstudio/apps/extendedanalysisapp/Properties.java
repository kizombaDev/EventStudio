package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.kizombadev.eventstudio.common.PropertyHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("extended-analysis")
public class Properties {
    private static final String PROPERTY_ROOT = "extended-analysis.";

    private int maxHistoricalEvents;
    private int percentile;
    private double interquartileFactor;

    public int getMaxHistoricalEvents() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "maxHistoricalEvents", maxHistoricalEvents);
        return maxHistoricalEvents;
    }

    public void setMaxHistoricalEvents(int maxHistoricalEvents) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "maxHistoricalEvents", maxHistoricalEvents);
        this.maxHistoricalEvents = maxHistoricalEvents;
    }

    public int getPercentile() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "percentile", percentile);
        return percentile;
    }

    public void setPercentile(int percentile) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "percentile", percentile);
        this.percentile = percentile;
    }

    public double getInterquartileFactor() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "interquartileFactor", interquartileFactor);
        return interquartileFactor;
    }

    public void setInterquartileFactor(double interquartileFactor) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "interquartileFactor", interquartileFactor);
        this.interquartileFactor = interquartileFactor;
    }
}
