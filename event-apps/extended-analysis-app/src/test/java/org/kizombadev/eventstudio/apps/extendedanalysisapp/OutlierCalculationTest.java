package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class OutlierCalculationTest {

    @Test
    public void testEvaluate() {
        Collection<Integer> historicalValues = Arrays.asList(9, 10, 11);
        boolean isOutlier = OutlierCalculation.evaluate(historicalValues, 10, 25, 1.5);
        Assert.assertFalse(isOutlier);
    }

    @Test
    public void testEvaluateOutlier() {
        Collection<Integer> historicalValues = Arrays.asList(9, 10, 11);
        boolean isOutlier = OutlierCalculation.evaluate(historicalValues, 13, 25, 1.5);
        Assert.assertTrue(isOutlier);
    }

    @Test
    public void testEvaluatePercentile() {
        Collection<Integer> historicalValues = Arrays.asList(9, 10, 11);
        boolean isOutlier = OutlierCalculation.evaluate(historicalValues, 13, 10, 1.5);
        Assert.assertFalse(isOutlier);
    }

    @Test
    public void testEvaluateInterquartileFactor() {
        Collection<Integer> historicalValues = Arrays.asList(9, 10, 11);
        boolean isOutlier = OutlierCalculation.evaluate(historicalValues, 13, 25, 3);
        Assert.assertFalse(isOutlier);
    }
}