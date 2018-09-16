package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import java.util.Collection;

import static com.google.common.math.Quantiles.percentiles;

public final class OutlierCalculation {

    private OutlierCalculation() {
        //nothing to do
    }

    public static boolean evaluate(Collection<Integer> historicalValues, int nextValue, int percentile, double interquartileFactor) {

        if (historicalValues.isEmpty()) {
            return false;
        }

        double bottomBoundary = percentiles().index(percentile).compute(historicalValues);
        double topBoundary = percentiles().index(100 - percentile).compute(historicalValues);

        double interquartileRange = topBoundary - bottomBoundary;

        return nextValue < bottomBoundary - interquartileRange * interquartileFactor || nextValue > topBoundary + interquartileRange * interquartileFactor;
    }
}
